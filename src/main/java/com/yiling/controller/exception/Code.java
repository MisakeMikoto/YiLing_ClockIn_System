package com.yiling.controller.exception;

/**
 * @Author MisakiMikoto
 * @Date 2022/12/3 18:54
 */
public class Code {
       public static final Integer SAVE_ERR = 20010;
       public static final Integer DELETE_ERR = 20020;
       public static final Integer UPDATE_ERR = 20030;
       public static final Integer GET_ERR = 20040;
       public static final Integer INSERT_ERR = 20050;

       public static final Integer SAVE_OK = 20011;
       public static final Integer INSERT_OK = 20051;
       public static final Integer DELETE_OK = 20021;
       public static final Integer UPDATE_OK = 20031;
       public static final Integer GET_OK = 20041;

       public static final Integer SYSTEM_TIMEOUT_ERROR = 50001;
       public static final Integer SYSTEM_UNKNOW_ERROR = 59999;
       public static final Integer BUSINESS_ERROR = 50002;

}
