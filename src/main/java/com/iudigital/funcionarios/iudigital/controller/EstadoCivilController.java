/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iudigital.funcionarios.iudigital.controller;

import com.iudigital.funcionarios.iudigital.dao.EstadoCivilDao;
import com.iudigital.funcionarios.iudigital.dominio.GeneralPojo;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cesar
 */
public class EstadoCivilController {
    private EstadoCivilDao estadoCivilDao;
    
    public EstadoCivilController(){
        estadoCivilDao= new EstadoCivilDao();
    }
   
    public List<GeneralPojo> obtenerOpciones()throws SQLException{
        return estadoCivilDao.obtenerEstadosCivil();
    }
    
}
