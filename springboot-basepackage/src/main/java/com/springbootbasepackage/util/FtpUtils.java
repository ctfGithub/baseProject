package com.springbootbasepackage.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.Properties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

public class FtpUtils {
    private  static  final Logger logger= LoggerFactory.getLogger(FtpUtils.class);
    private  static final  int SESSSIONTIMEOUT=30*1000;//登录过期时间 30s
    private  static  final  int CHANNELTIMEOUT=60*1000;//通信通道过期时间 60s

    ///**
    // * 向FTP服务器上传文件
    // * @param url FTP服务器url
    // * @param port FTP服务器端口
    // * @param username FTP登录账号
    // * @param password FTP登录密码
    // * @param basePath FTP服务器基础目录 ,D:/fpt/
    // * @param filePath FTP服务器文件二级存放路径,test/2017/,文件的路径为basePath+filePath: D:/fpt/test/2017/
    // * @param filename 上传到FTP服务器上的文件名 :test.zip
    // * @param input 输入流
    // * @return 成功返回true，否则返回false
    // */

    public static boolean SSHSFTP_Upload(String ip, int port, String user, String psw, String basePath,
                                         String filePath, String filename) throws Exception {
        boolean flag=false;
        Session session = null;
        JSch jsch = new JSch();
        //登录远程服务
        try {
            if(port <=0){
                //连接服务器，采用默认端口
                session = jsch.getSession(user, ip);
            }else{
                //采用指定的端口连接服务器
                session = jsch.getSession(user, ip ,port);
            }

            //如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new Exception("ssh Sftp服务器session is null");
            }

            //设置登陆主机的密码
            session.setPassword(psw);//设置密码
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config); // 为Session对象设置properties
            //设置登陆超时时间
            session.connect(SESSSIONTIMEOUT);
            logger.info("远程SFTP服务连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("远程SFTP服务登录失败");
        }

        Channel channel = null;
        //文件上传到服务器
        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(CHANNELTIMEOUT);
            ChannelSftp sftp = (ChannelSftp) channel;
            //这里是重点，之前在网上看过其他人的上传文件的代码，踩过坑，
            //这里的put(String src, String dst, int mode)上传文件的方法详解：src代表本地要上传的文件路径，dst代表上传到服务器的路径，mode文件传输模式
//            sftp.put(basePath+filePath+"/"+filename, "/", ChannelSftp.OVERWRITE);
            sftp.put(filePath+filename, basePath, ChannelSftp.OVERWRITE);
            logger.info("文件地址："+filePath+filename);
            logger.info("SFTP  文件传输结束");
            sftp.quit();
            return  true;
        } catch (Exception e) {
            logger.error("上传文件到SFTP时异常:{}",e);
            logger.info("上传文件到SFTP时异常:{}",e);
        } finally {
            //关闭服务
            try{
                if (channel.isConnected()) {
                    channel.disconnect();
                    channel = null;
                    logger.info("SFTP，Channel已关闭");
                }

                if (session.isConnected()) {
                    session.disconnect();
                    session = null;
                    logger.info("SFTP，Session服务已关闭");
                }
            }catch (Exception e){
                logger.error("上传文件到SFTP时异常:{}",e);
                logger.info("上传文件到SFTP时异常:{}",e);
            }
        }
        return  flag;
    }

    public static void main(String[] args) {
        String basePath="/upload/zxBankPackage/custInfo";
        String filePath="C:\\文档\\测试zip\\";
        String filename="测试使用.zip";
        try {
            FtpUtils.SSHSFTP_Upload("47.97.181.47",22,"sftpuser", "Ss_zx#0818$",basePath,filePath,filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
