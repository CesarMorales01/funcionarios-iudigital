/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iudigital.funcionarios.iudigital.dominio;

/**
 *
 * @author Cesar
 */
public class Funcionario {
    
    private String nombre, apellidos, fechanacimiento, direccion;
    private int id_funcionario, estado_civil, sexo, cedulafuncionario, telefono, tipoDocumento;

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(int estado_civil) {
        this.estado_civil = estado_civil;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }
  
    public int getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getCedulafuncionario() {
        return cedulafuncionario;
    }

    public void setCedulafuncionario(int cedulafuncionario) {
        this.cedulafuncionario = cedulafuncionario;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombre + ": " + cedulafuncionario;
    }  

/*
    @Override
    public String toString() {
        return "Funcionario{" + "nombre=" + nombre + ", apellidos=" + apellidos + ", fechanacimiento=" + fechanacimiento + ", direccion=" + direccion + ", id_funcionario=" + id_funcionario + ", estado_civil=" + estado_civil + ", sexo=" + sexo + ", cedulafuncionario=" + cedulafuncionario + ", telefono=" + telefono + ", tipoDocumento=" + tipoDocumento + '}';
    }
*/
    
    
}
