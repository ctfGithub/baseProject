package com.springbootbasepackage.service;

import com.springbootbasepackage.dto.SqlAffectedResult;
import com.springbootbasepackage.dto.SqlExecuteDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL 影响行数检测服务（公用 API）
 * <p>
 * 使用 MyBatis-Plus SqlSessionFactory 打开手动会话（autoCommit=false），
 * 执行 SQL 拿影响行数后 rollback，数据不会真正变更。
 * 支持传入 databaseName 切库（USE），SQL 中可不带库名。
 * 语法错误的 SQL 返回 "错误语法"，不影响下一条。
 */
@Slf4j
@Service
public class SqlAffectedRowsService {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 按 ; 切割 SQL，逐条检测影响行数（共用同一个 Session）
     */
    public List<SqlAffectedResult> check(SqlExecuteDTO dto) {
        List<SqlAffectedResult> results = new ArrayList<>();

        if (dto.getSql() == null || dto.getSql().trim().isEmpty()) {
            return results;
        }

        // 按 ; 切割
        String[] sqlArray = dto.getSql().split(";");

        // 共用一个 Session，这样 USE 切库对所有 SQL 生效
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.SIMPLE, false)) {
            Connection conn = session.getConnection();

            // 如果传了 databaseName，先切库
            String databaseName = dto.getDatabaseName();
            if (databaseName != null && !databaseName.trim().isEmpty()) {
                try (PreparedStatement usePstmt = conn.prepareStatement("USE " + databaseName.trim())) {
                    usePstmt.execute();
                }
                log.info("[SQL检测] 已切库: USE {}", databaseName.trim());
            }

            // 逐条执行
            for (String rawSql : sqlArray) {
                String sql = rawSql.trim();
                if (sql.isEmpty()) {
                    continue;
                }
                sql = sql.replaceAll("\\s+", " ").trim();
                results.add(checkSingle(conn, sql));
            }

        } catch (Exception e) {
            log.error("[SQL检测] 会话异常: {}", e.getMessage());
        }

        return results;
    }

    /**
     * 单条 SQL 检测：执行 → 拿影响行数 → rollback
     */
    private SqlAffectedResult checkSingle(Connection conn, String sql) {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            boolean isQuery = pstmt.execute();
            int affectedRows;

            if (isQuery) {
                // SELECT：统计结果行数
                affectedRows = 0;
                while (pstmt.getResultSet().next()) {
                    affectedRows++;
                }
            } else {
                // INSERT / UPDATE / DELETE / DDL
                affectedRows = pstmt.getUpdateCount();
            }

            // rollback，不提交 → 数据不会真正变更
            conn.rollback();

            log.info("[SQL检测] 影响行数={} | {}", affectedRows, sql);
            return SqlAffectedResult.ok(sql, affectedRows);

        } catch (Exception e) {
            // 报错：返回"错误语法"，不影响下一条
            log.warn("[SQL检测] 语法错误: {} | {}", e.getMessage(), sql);
            return SqlAffectedResult.fail(sql);
        }
    }
}
