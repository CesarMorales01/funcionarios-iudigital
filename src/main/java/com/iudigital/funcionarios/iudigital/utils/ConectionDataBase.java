/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iudigital.funcionarios.iudigital.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Cesar
 */
public class ConectionDataBase {
    private static final String URL="jdbc:postgresql://localhost:5432/FuncionariosDAO";
    private static final String USER= "postgres";
    private static final String PASSWORD="0";
    
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    
    
}
