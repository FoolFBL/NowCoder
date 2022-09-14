package com.kong.newcoder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author shijiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginTicket {
    private int id;
    private int userid;
    private String ticket;
    private int status;
    private Date expired;
}
