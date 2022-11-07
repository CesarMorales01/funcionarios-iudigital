/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iudigital.funcionarios.iudigital.controller;

import com.iudigital.funcionarios.iudigital.dao.FuncionariosDao;
import com.iudigital.funcionarios.iudigital.dominio.Funcionario;
import com.iudigital.funcionarios.iudigital.dominio.GeneralPojo;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Cesar
 */
public class FuncionariosController {
    private final FuncionariosDao funcionarioDao;
    
    public FuncionariosController(){
        funcionarioDao= new FuncionariosDao();
    }
    
    public  String crearFuncionario(Funcionario funcionario) throws SQLException{
       return funcionarioDao.crear(funcionario);
    }
    
    public List<Funcionario> obtenerFuncionarios()throws SQLException{
      return funcionarioDao.obtenerFuncionarios();
    }
    
    public Funcionario obtenerFuncionario(int id) throws SQLException{
        return funcionarioDao.obtenerFuncionario(id);
    }
    
    public String actualizarFuncionario(Funcionario funcionario) throws SQLException{
        return funcionarioDao.actualizar(funcionario);
    }
    
    public String eliminarFuncionario(int id) throws SQLException{
       return funcionarioDao.eliminar(id);
    }
    
    public GeneralPojo obtenerTelefonoFuncionario(int id) throws SQLException{
       return funcionarioDao.obtenerTelefonoFuncionario(id);
    }
    
    public void GuardarTelefonoFuncionario(int tel, int ced) throws SQLException{
       funcionarioDao.guardarTelefono(tel, ced);
    }
    
    public  String EliminarTelefono(int ced) throws SQLException{
       return funcionarioDao.eliminarTelefonos(ced);
    }
    
    public  String ActualizarTelefono(Funcionario tel) throws SQLException{
       return funcionarioDao.updateTelefono(tel);
    }
}
