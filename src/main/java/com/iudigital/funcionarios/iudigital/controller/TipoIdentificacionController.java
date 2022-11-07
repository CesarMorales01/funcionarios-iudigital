/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iudigital.funcionarios.iudigital.controller;

import com.iudigital.funcionarios.iudigital.dao.SexoDao;
import com.iudigital.funcionarios.iudigital.dao.TipoIdentificacionDao;
import com.iudigital.funcionarios.iudigital.dominio.GeneralPojo;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cesar
 */
public class TipoIdentificacionController {
     private TipoIdentificacionDao tipoIdentificacionDao;

    public TipoIdentificacionController() {
        tipoIdentificacionDao = new TipoIdentificacionDao();
    } 
   
    public List<GeneralPojo> obtenerOpciones()throws SQLException{
        return tipoIdentificacionDao.obtenerGeneros();
    }
    
}
