/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iudigital.funcionarios.iudigital.dao;

import com.iudigital.funcionarios.iudigital.dominio.GeneralPojo;
import com.iudigital.funcionarios.iudigital.utils.ConectionDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cesar
 */
public class TipoIdentificacionDao {
    
    private static final String GET_TIPOS="select * from tipo_identificacion";
    
     public  List<GeneralPojo> obtenerGeneros() throws SQLException{
        Connection conection = null;
        PreparedStatement preparedStatement= null;
        ResultSet resultSet = null;
        List<GeneralPojo> opciones= new ArrayList<>(); 
        try{
            conection= ConectionDataBase.getConnection();
            preparedStatement= conection.prepareStatement(GET_TIPOS);
            resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                GeneralPojo generalPojo= new GeneralPojo();
                generalPojo.setId(resultSet.getInt("id_tipo"));
                generalPojo.setNombre(resultSet.getString("tipo_identificacion"));
                opciones.add(generalPojo);  
            }
            return opciones;
        }finally{
           if(conection!=null){
                conection.close();
            }
            if(preparedStatement!=null){
                preparedStatement.close();
            }
            if(resultSet!=null){
                resultSet.close();
            }
        }
    }
    
}
