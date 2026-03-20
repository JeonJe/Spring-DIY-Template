package com.diy.framework.web.view;

import com.diy.framework.web.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


// 렌더링을 담당하는데 데이터는 model에 담겨있음
public interface View {
    void render(Map<String, Object> model,
                HttpServletRequest request,
                HttpServletResponse response) throws
            Exception;
}
