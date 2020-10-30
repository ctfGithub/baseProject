package com.springbootbasepackage.base;

import java.util.List;
import java.util.Map;

public interface Export {
    <T> byte[] export(List<T> var1, Class<T> var2) throws Exception;

    <T> byte[] export(List<T> var1, Class<T> var2, Map<String, String> var3) throws Exception;

    byte[] export(List<List<Object>> var1, List<Class> var2, List<Map<String, String>> var3, List<String> var4) throws Exception;
}
