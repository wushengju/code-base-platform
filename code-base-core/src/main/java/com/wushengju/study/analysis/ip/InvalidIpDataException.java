package com.wushengju.study.analysis.ip;

import java.io.IOException;

/**
 * IP源数据异常
 *
 * @author Sunny
 * @version 1.0
 * @className InvalidIpDataException
 * @date 2019-11-19 10:47
 */
public class InvalidIpDataException extends IOException {
    private static final long serialVersionUID = 7818375828106090155L;

    public InvalidIpDataException(String message) {
        super(message);
    }
}
