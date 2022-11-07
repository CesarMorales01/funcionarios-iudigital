package com.iudigital.funcionarios.iudigital.presentacion;

import com.iudigital.funcionarios.iudigital.controller.EstadoCivilController;
import com.iudigital.funcionarios.iudigital.controller.FuncionariosController;
import com.iudigital.funcionarios.iudigital.controller.SexoController;
import com.iudigital.funcionarios.iudigital.controller.TipoIdentificacionController;
import com.iudigital.funcionarios.iudigital.dominio.Funcionario;
import com.iudigital.funcionarios.iudigital.dominio.GeneralPojo;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Cesar
 */
public class Main extends javax.swing.JFrame {
    private final FuncionariosController funcionariosController;
    private final SexoController sexoController;
    private final TipoIdentificacionController tipoIdentificacionController;
    private final EstadoCivilController estadoCivilController;
    private static final String[] COLUMNS= {"T. DOCUMENTO", "DOCUMENTO", "NOMBRE", "APELLIDOS", "DIRECCION", "TELEFONO", "FECHA DE NACIMIENTO", "SEXO", "ESTADO CIVIL"};
    private static final String SELECCIONE= "--Seleccione--";
    List<Funcionario> funcionarios;
    List<GeneralPojo> generos;
    List<GeneralPojo> tiposIdentificacion;
    List<GeneralPojo> estadosCivil;
    
    public Main() {
        initComponents();
        funcionariosController= new FuncionariosController();
        sexoController= new SexoController();
        tipoIdentificacionController= new TipoIdentificacionController();
        estadoCivilController= new EstadoCivilController();
        //se carga opciones sexo, tipo identificacion y funcionarios despues de cargarOpcionesEstadoCivil...
        // Esto porque se debe cargar las lists de opciones para despues buscar los nombres de sexo, tipo identificacion, etc.
        cargarOpcionesEstadoCivil();
        addListener();
    }
    
     private void cargarOpcionesTipoIdentificacion(){
        CBTipoDocumento.removeAllItems();
        CBTipoDocumentoEdit.removeAllItems();
        CBTipoDocumento.addItem(SELECCIONE);
        CBTipoDocumentoEdit.addItem(SELECCIONE);
        try {
            tiposIdentificacion=tipoIdentificacionController.obtenerOpciones();
            for(int i=0; i<tiposIdentificacion.size(); i++){
                CBTipoDocumento.addItem(tiposIdentificacion.get(i).getNombre());
                CBTipoDocumentoEdit.addItem(tiposIdentificacion.get(i).getNombre());
            }
            // se carga funcionarios despues de opciones sexo
            cargarOpcionesSexo();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cargarOpcionesEstadoCivil(){
        CBEstadoCivil.removeAllItems();
        CBEstadoCivilEdit.removeAllItems();
        CBEstadoCivil.addItem(SELECCIONE);
        CBEstadoCivilEdit.addItem(SELECCIONE);
        try {
            estadosCivil= estadoCivilController.obtenerOpciones();
            for(int i=0; i<estadosCivil.size(); i++){
                CBEstadoCivil.addItem(estadosCivil.get(i).getNombre());
                CBEstadoCivilEdit.addItem(estadosCivil.get(i).getNombre());
            }
            cargarOpcionesTipoIdentificacion();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cargarOpcionesSexo(){
        CBSexo.removeAllItems();
        CBSexoEdit.removeAllItems();
        CBSexo.addItem(SELECCIONE);
        CBSexoEdit.addItem(SELECCIONE);
        try {
            generos=sexoController.obtenerOpciones();
            for(int i=0; i<generos.size(); i++){
                CBSexo.addItem(generos.get(i).getNombre());
                CBSexoEdit.addItem(generos.get(i).getNombre());
            }
            cargarFuncionarios();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarFuncionarios(){
        TFCedulaEdit.setEnabled(false);
        CBFuncionarios.removeAllItems();
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNombre(SELECCIONE);
        CBFuncionarios.addItem(funcionario1);
        
        TableFuncionarios.removeAll();
        DefaultTableModel defaultTableModel= new DefaultTableModel();
        for (String COLUMN: COLUMNS){
            defaultTableModel.addColumn(COLUMN);
        }
        TableFuncionarios.setModel(defaultTableModel);
        try {
            funcionarios= funcionariosController.obtenerFuncionarios();
            if(funcionarios.isEmpty()){
                return;
            }
            defaultTableModel.setRowCount(funcionarios.size());
            int row=0;
            for(Funcionario funcionario: funcionarios){
                String tipoDoc = getNombreTD(funcionario.getTipoDocumento());
                defaultTableModel.setValueAt(tipoDoc, row, 0);
                defaultTableModel.setValueAt(funcionario.getCedulafuncionario(), row, 1);
                defaultTableModel.setValueAt(funcionario.getNombre(), row, 2);
                defaultTableModel.setValueAt(funcionario.getApellidos(), row, 3);
                defaultTableModel.setValueAt(funcionario.getDireccion(), row, 4);
                GeneralPojo tel =funcionariosController.obtenerTelefonoFuncionario(funcionario.getCedulafuncionario());
                if(tel!=null){
                   defaultTableModel.setValueAt(tel.getNombre(), row, 5); 
                   funcionario.setTelefono(Integer.parseInt(tel.getNombre()));
                }
                defaultTableModel.setValueAt(funcionario.getFechanacimiento(), row, 6);
                String sexo = getNombreSexo(funcionario.getSexo());
                defaultTableModel.setValueAt(sexo, row, 7);
                String estadoCivil= getNombreEstadoCivil(funcionario.getEstado_civil());
                defaultTableModel.setValueAt(estadoCivil, row, 8);
                row++;
                CBFuncionarios.addItem(funcionario);
            }
            LabelLoading.setVisible(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }  
    }
    
    private String getNombreTD(int id){
     String tipo="";
        for(int i=0; i<tiposIdentificacion.size();i++){
            if(tiposIdentificacion.get(i).getId()==id){
                tipo=tiposIdentificacion.get(i).getNombre();
            }
        }
     return tipo;
    }
    
     private String getNombreSexo(int id){
     String sex="";
        for(int i=0; i<generos.size();i++){
            if(generos.get(i).getId()==id){
                sex=generos.get(i).getNombre();
            }
        }
     return sex;
    }
    
    private String getNombreEstadoCivil(int id){
     String estado="";
        for(int i=0; i<estadosCivil.size();i++){
            if(estadosCivil.get(i).getId()==id){
                estado=estadosCivil.get(i).getNombre();
            }
        }
     return estado;
    }
       
    private void addListener(){
        CBFuncionarios.addItemListener(event ->{
            Funcionario funcionario = (Funcionario)event.getItem();
            if(funcionario.getNombre().equalsIgnoreCase(SELECCIONE)){
                limpiarCamposEdit();
            }else{
                TFNombresEdit.setText(funcionario.getNombre());
                TFApellidosEdit.setText(funcionario.getApellidos());
                TFCedulaEdit.setText(String.valueOf(funcionario.getCedulafuncionario()));
                TFDireccionEdit.setText(funcionario.getDireccion());
                TFTelefonoEdit.setText(String.valueOf(funcionario.getTelefono()));
                TFFechaNacimientoEdit.setText(funcionario.getFechanacimiento());
                setValorCBTipoDocumento(funcionario.getTipoDocumento());
                setValorCBSexo(funcionario.getSexo());
                setValorCBEstadoCivil(funcionario.getEstado_civil());
            }
        });
    }
    
     private void setValorCBEstadoCivil(int idEstado){
        String estadoFunc="";
        for(int i=0; i<estadosCivil.size(); i++){
             if(estadosCivil.get(i).getId()==idEstado){
                 estadoFunc=estadosCivil.get(i).getNombre();
             }
        }
        for(int i=0; i<CBEstadoCivilEdit.getItemCount(); i++){
            if(estadoFunc.equalsIgnoreCase(CBEstadoCivilEdit.getItemAt(i))){
                CBEstadoCivilEdit.setSelectedIndex(i);
            }
        }
    }
    
     private void setValorCBSexo(int idSexo){
        String sexoFunc="";
        for(int i=0; i<generos.size(); i++){
             if(generos.get(i).getId()==idSexo){
                 sexoFunc=generos.get(i).getNombre();
             }
        }
        for(int i=0; i<CBSexoEdit.getItemCount(); i++){
            if(sexoFunc.equalsIgnoreCase(CBSexoEdit.getItemAt(i))){
                CBSexoEdit.setSelectedIndex(i);
            }
        }
    }
    
    private void setValorCBTipoDocumento(int idTC){
        String TCFunc="";
        for(int i=0; i<tiposIdentificacion.size(); i++){
             if(tiposIdentificacion.get(i).getId()==idTC){
                 TCFunc=tiposIdentificacion.get(i).getNombre();
                 
             }
        }
        for(int i=0; i<CBTipoDocumentoEdit.getItemCount(); i++){
            if(TCFunc.equalsIgnoreCase(CBTipoDocumentoEdit.getItemAt(i))){
                CBTipoDocumentoEdit.setSelectedIndex(i);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        PanelCrear = new javax.swing.JTabbedPane();
        jPFuncionario = new javax.swing.JPanel();
        jPFuncionarios1 = new javax.swing.JPanel();
        LabelNombre = new javax.swing.JLabel();
        LabelApellidos = new javax.swing.JLabel();
        LabelCedula = new javax.swing.JLabel();
        TFNombres = new javax.swing.JTextField();
        TFApellidos = new javax.swing.JTextField();
        TFTelefono = new javax.swing.JTextField();
        BtnCrear = new javax.swing.JButton();
        CBEstadoCivil = new javax.swing.JComboBox<>();
        LabelSexo = new javax.swing.JLabel();
        LabelEstadoCivil = new javax.swing.JLabel();
        CBSexo = new javax.swing.JComboBox<>();
        LabelDireccion = new javax.swing.JLabel();
        TFDireccion = new javax.swing.JTextField();
        LabelFechaNacimiento = new javax.swing.JLabel();
        LabelTelefono = new javax.swing.JLabel();
        TFCedula = new javax.swing.JTextField();
        TFFechaNacimiento = new javax.swing.JTextField();
        LabelTipoDocumento = new javax.swing.JLabel();
        CBTipoDocumento = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableFuncionarios = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        LabelLoading = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        PanelEditar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        CBFuncionarios = new javax.swing.JComboBox<>();
        jPFuncionarios3 = new javax.swing.JPanel();
        LabelNombre2 = new javax.swing.JLabel();
        LabelApellidos2 = new javax.swing.JLabel();
        LabelCedula2 = new javax.swing.JLabel();
        TFNombresEdit = new javax.swing.JTextField();
        TFApellidosEdit = new javax.swing.JTextField();
        TFTelefonoEdit = new javax.swing.JTextField();
        BtnEditar = new javax.swing.JButton();
        CBEstadoCivilEdit = new javax.swing.JComboBox<>();
        LabelSexo2 = new javax.swing.JLabel();
        LabelEstadoCivil2 = new javax.swing.JLabel();
        CBSexoEdit = new javax.swing.JComboBox<>();
        LabelDireccion2 = new javax.swing.JLabel();
        TFDireccionEdit = new javax.swing.JTextField();
        LabelFechaNacimiento2 = new javax.swing.JLabel();
        LabelTelefono2 = new javax.swing.JLabel();
        TFCedulaEdit = new javax.swing.JTextField();
        TFFechaNacimientoEdit = new javax.swing.JTextField();
        LabelTipoDocumento2 = new javax.swing.JLabel();
        CBTipoDocumentoEdit = new javax.swing.JComboBox<>();
        BtnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Gestión funcionarios IUDigital");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, -1));

        PanelCrear.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPFuncionarios1.setBorder(javax.swing.BorderFactory.createTitledBorder("Digite los siguientes campos"));

        LabelNombre.setText("Nombres");

        LabelApellidos.setText("Apellidos");

        LabelCedula.setText("N° Documento");

        TFNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFNombresActionPerformed(evt);
            }
        });

        TFApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFApellidosActionPerformed(evt);
            }
        });

        TFTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFTelefonoActionPerformed(evt);
            }
        });

        BtnCrear.setText("Guardar");
        BtnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCrearActionPerformed(evt);
            }
        });

        LabelSexo.setText("Sexo");

        LabelEstadoCivil.setText("Estado civil");

        LabelDireccion.setText("Dirección");

        TFDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFDireccionActionPerformed(evt);
            }
        });

        LabelFechaNacimiento.setText("Fecha de nacimiento");

        LabelTelefono.setText("Telefono");

        TFCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFCedulaActionPerformed(evt);
            }
        });

        TFFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFFechaNacimientoActionPerformed(evt);
            }
        });

        LabelTipoDocumento.setText("Tipo Documento");

        CBTipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBTipoDocumentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPFuncionarios1Layout = new javax.swing.GroupLayout(jPFuncionarios1);
        jPFuncionarios1.setLayout(jPFuncionarios1Layout);
        jPFuncionarios1Layout.setHorizontalGroup(
            jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TFDireccion)
                    .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                        .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                                .addComponent(TFNombres)
                                .addGap(46, 46, 46))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarios1Layout.createSequentialGroup()
                                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelDireccion)
                                    .addComponent(LabelNombre))
                                .addGap(159, 159, 159)))
                        .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelApellidos)
                            .addComponent(TFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                        .addGap(0, 67, Short.MAX_VALUE)
                        .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelFechaNacimiento)
                            .addComponent(TFFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelTipoDocumento))
                        .addGap(37, 37, 37)
                        .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarios1Layout.createSequentialGroup()
                                        .addComponent(TFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                                        .addComponent(LabelCedula)
                                        .addGap(69, 69, 69)))
                                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CBSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelTelefono))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CBEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(BtnCrear))
                .addGap(43, 43, 43))
        );
        jPFuncionarios1Layout.setVerticalGroup(
            jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelNombre)
                    .addComponent(LabelApellidos)
                    .addComponent(LabelCedula)
                    .addComponent(LabelTipoDocumento)
                    .addComponent(LabelSexo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPFuncionarios1Layout.createSequentialGroup()
                        .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarios1Layout.createSequentialGroup()
                        .addComponent(CBTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelDireccion)
                    .addComponent(LabelFechaNacimiento)
                    .addComponent(LabelTelefono)
                    .addComponent(LabelEstadoCivil))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPFuncionarios1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnCrear)
                .addContainerGap())
        );

        TableFuncionarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(TableFuncionarios);

        jLabel3.setText("Funcionarios registrados");

        LabelLoading.setText("Cargando información...");

        javax.swing.GroupLayout jPFuncionarioLayout = new javax.swing.GroupLayout(jPFuncionario);
        jPFuncionario.setLayout(jPFuncionarioLayout);
        jPFuncionarioLayout.setHorizontalGroup(
            jPFuncionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFuncionarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPFuncionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPFuncionarios1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPFuncionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelLoading)
                    .addComponent(jLabel3))
                .addGap(379, 379, 379))
        );
        jPFuncionarioLayout.setVerticalGroup(
            jPFuncionarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFuncionarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPFuncionarios1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelLoading)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PanelCrear.addTab("Crear funcionario", jPFuncionario);

        jLabel2.setText("Funcionarios");

        jPFuncionarios3.setBorder(javax.swing.BorderFactory.createTitledBorder("Digite los siguientes campos"));

        LabelNombre2.setText("Nombres");

        LabelApellidos2.setText("Apellidos");

        LabelCedula2.setText("N° Documento");

        TFNombresEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFNombresEditActionPerformed(evt);
            }
        });

        TFApellidosEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFApellidosEditActionPerformed(evt);
            }
        });

        TFTelefonoEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFTelefonoEditActionPerformed(evt);
            }
        });

        BtnEditar.setText("Editar ");
        BtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditarActionPerformed(evt);
            }
        });

        LabelSexo2.setText("Sexo");

        LabelEstadoCivil2.setText("Estado civil");

        LabelDireccion2.setText("Dirección");

        TFDireccionEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFDireccionEditActionPerformed(evt);
            }
        });

        LabelFechaNacimiento2.setText("Fecha de nacimiento");

        LabelTelefono2.setText("Telefono");

        TFCedulaEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFCedulaEditActionPerformed(evt);
            }
        });

        TFFechaNacimientoEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFFechaNacimientoEditActionPerformed(evt);
            }
        });

        LabelTipoDocumento2.setText("Tipo Documento");

        CBTipoDocumentoEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBTipoDocumentoEditActionPerformed(evt);
            }
        });

        BtnEliminar.setForeground(new java.awt.Color(255, 0, 51));
        BtnEliminar.setText("Eliminar");
        BtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPFuncionarios3Layout = new javax.swing.GroupLayout(jPFuncionarios3);
        jPFuncionarios3.setLayout(jPFuncionarios3Layout);
        jPFuncionarios3Layout.setHorizontalGroup(
            jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                        .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelDireccion2)
                            .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                                .addComponent(LabelNombre2)
                                .addGap(131, 131, 131)
                                .addComponent(LabelApellidos2)))
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarios3Layout.createSequentialGroup()
                        .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TFDireccionEdit)
                            .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                                .addComponent(TFNombresEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                .addGap(46, 46, 46)
                                .addComponent(TFApellidosEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                        .addGap(0, 67, Short.MAX_VALUE)
                        .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelFechaNacimiento2)
                            .addComponent(TFFechaNacimientoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBTipoDocumentoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelTipoDocumento2))
                        .addGap(37, 37, 37)
                        .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarios3Layout.createSequentialGroup()
                                        .addComponent(TFCedulaEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                                        .addComponent(LabelCedula2)
                                        .addGap(69, 69, 69)))
                                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabelSexo2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CBSexoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TFTelefonoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelTelefono2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CBEstadoCivilEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LabelEstadoCivil2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                        .addComponent(BtnEditar)
                        .addGap(313, 313, 313)
                        .addComponent(BtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jPFuncionarios3Layout.setVerticalGroup(
            jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelNombre2)
                    .addComponent(LabelApellidos2)
                    .addComponent(LabelCedula2)
                    .addComponent(LabelTipoDocumento2)
                    .addComponent(LabelSexo2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPFuncionarios3Layout.createSequentialGroup()
                        .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TFNombresEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFApellidosEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TFCedulaEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CBSexoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPFuncionarios3Layout.createSequentialGroup()
                        .addComponent(CBTipoDocumentoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelDireccion2)
                    .addComponent(LabelFechaNacimiento2)
                    .addComponent(LabelTelefono2)
                    .addComponent(LabelEstadoCivil2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFDireccionEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFTelefonoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFFechaNacimientoEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CBEstadoCivilEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPFuncionarios3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnEditar)
                    .addComponent(BtnEliminar))
                .addContainerGap())
        );

        javax.swing.GroupLayout PanelEditarLayout = new javax.swing.GroupLayout(PanelEditar);
        PanelEditar.setLayout(PanelEditarLayout);
        PanelEditarLayout.setHorizontalGroup(
            PanelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEditarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PanelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(CBFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(754, Short.MAX_VALUE))
            .addGroup(PanelEditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPFuncionarios3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelEditarLayout.setVerticalGroup(
            PanelEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CBFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPFuncionarios3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(167, 167, 167))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(PanelEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelCrear.addTab("Editar funcionario", jPanel2);

        getContentPane().add(PanelCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 890, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TFNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFNombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFNombresActionPerformed

    private void TFApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFApellidosActionPerformed

    private void TFTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFTelefonoActionPerformed

    private void BtnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCrearActionPerformed
 
       if(validarCamposVacios()==9){
            String nombre = TFNombres.getText();
            String apellidos = TFApellidos.getText();
            String cedula = TFCedula.getText();
            String fechanacimiento = TFFechaNacimiento.getText();
            String direccion = TFDireccion.getText();
            String telefono= TFTelefono.getText();
            int tel = 0;
            try {
                tel = Integer.parseInt(telefono);
             }
             catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Solo se aceptan números en el campo télefono!");
                return;
             }
            int ced = 0;
            try {
                ced = Integer.parseInt(cedula);
             }
             catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Solo se aceptan números en el campo cédula!");
                return;
             }
            int estadoC = 0;
             for(int i=0; i<estadosCivil.size();i++){
                 if(estadosCivil.get(i).getNombre()==CBEstadoCivil.getSelectedItem()){
                    estadoC=estadosCivil.get(i).getId();
                 }
             }
             int idSexo = 0;
             for(int i=0; i<generos.size();i++){
                 if(generos.get(i).getNombre()==CBSexo.getSelectedItem()){
                    idSexo=generos.get(i).getId();
                 }
             }
             int tipoI = 0;
             for(int i=0; i<tiposIdentificacion.size();i++){
                 if(tiposIdentificacion.get(i).getNombre()==CBTipoDocumento.getSelectedItem()){
                    tipoI=tiposIdentificacion.get(i).getId();
                 }
             }
            
            Funcionario funcionario = new Funcionario();
            funcionario.setTipoDocumento(tipoI);
            funcionario.setEstado_civil(estadoC);
            funcionario.setSexo(idSexo);
            funcionario.setNombre(nombre);
            funcionario.setApellidos(apellidos);
            funcionario.setCedulafuncionario(ced);
            funcionario.setFechanacimiento(fechanacimiento);
            funcionario.setDireccion(direccion);
            funcionario.setTelefono(tel);
            try{
                if(ced!=0){
                    String resp=funcionariosController.crearFuncionario(funcionario);
                    System.out.println(resp);
                    if("Creado".equals(resp)){
                        funcionariosController.GuardarTelefonoFuncionario(funcionario.getTelefono(), funcionario.getCedulafuncionario());
                    }
                    cargarFuncionarios();
                    limpiarCampos();
                }
            }catch (SQLException ex){
                ex.printStackTrace();
            } 
       }
        
    }//GEN-LAST:event_BtnCrearActionPerformed

    private void limpiarCampos(){
        TFCedula.setText("");
        TFNombres.setText("");
        TFApellidos.setText("");
        TFDireccion.setText("");
        TFFechaNacimiento.setText("");
        TFTelefono.setText("");
    }
    
    private void TFDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFDireccionActionPerformed

    private void TFCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFCedulaActionPerformed

    private void TFFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFFechaNacimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFFechaNacimientoActionPerformed

    private void CBTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBTipoDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBTipoDocumentoActionPerformed

    private void TFNombresEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFNombresEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFNombresEditActionPerformed

    private void TFApellidosEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFApellidosEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFApellidosEditActionPerformed

    private void TFTelefonoEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFTelefonoEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFTelefonoEditActionPerformed

    private void BtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditarActionPerformed

        if(validarCamposVaciosEdit()==9){
            String nombre = TFNombresEdit.getText();
            String apellidos = TFApellidosEdit.getText();
            String cedula = TFCedulaEdit.getText();
            String fechanacimiento = TFFechaNacimientoEdit.getText();
            String direccion = TFDireccionEdit.getText();
            String telefono= TFTelefonoEdit.getText();
            int tel;
            try {
                tel = Integer.parseInt(telefono);
             }
             catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Solo se aceptan números en el campo télefono!");
                return;
             }
            
            int ced;
            try {
                ced = Integer.parseInt(cedula);
             }
             catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Solo se aceptan números en el campo cédula!");
                return;
             }
            int estadoC = 0;
             for(int i=0; i<estadosCivil.size();i++){
                 if(estadosCivil.get(i).getNombre()==CBEstadoCivilEdit.getSelectedItem()){
                    estadoC=estadosCivil.get(i).getId();
                 }
             }
             int idSexo = 0;
             for(int i=0; i<generos.size();i++){
                 if(generos.get(i).getNombre()==CBSexoEdit.getSelectedItem()){
                    idSexo=generos.get(i).getId();
                 }
             }
             int tipoI = 0;
             for(int i=0; i<tiposIdentificacion.size();i++){
                 if(tiposIdentificacion.get(i).getNombre()==CBTipoDocumentoEdit.getSelectedItem()){
                    tipoI=tiposIdentificacion.get(i).getId();
                 }
             }
            
            Funcionario funcionario = new Funcionario();
            funcionario.setTipoDocumento(tipoI);
            funcionario.setEstado_civil(estadoC);
            funcionario.setSexo(idSexo);
            funcionario.setNombre(nombre);
            funcionario.setApellidos(apellidos);
            funcionario.setCedulafuncionario(ced);
            funcionario.setFechanacimiento(fechanacimiento);
            funcionario.setDireccion(direccion);
            funcionario.setTelefono(tel);
            try{
                if(ced!=0){
                    String resp=funcionariosController.actualizarFuncionario(funcionario);
                    System.out.println(resp);
                    if("Actualizado".equals(resp)){
                        Funcionario telef= new Funcionario();
                        telef.setCedulafuncionario(funcionario.getCedulafuncionario());
                        telef.setTelefono(funcionario.getTelefono());
                        funcionariosController.ActualizarTelefono(telef);
                        JOptionPane.showMessageDialog(null, "Funcionario actualizado!");
                    }
                    cargarFuncionarios();
                }
            }catch (SQLException ex){
                ex.printStackTrace();
            } 
       }
    }//GEN-LAST:event_BtnEditarActionPerformed

    private int validarCamposVaciosEdit(){
        int validados=0;
       if(TFCedulaEdit.getText().trim().length()==0){
           JOptionPane.showMessageDialog(null, "Campo cédula vacio!");
        }else{
          validados++; 
       }
       if(TFNombresEdit.getText().trim().length()==0){
           JOptionPane.showMessageDialog(null, "Campo nombres vacio!");
        }else{
           validados++;
       }
       if(TFApellidosEdit.getText().trim().length()==0){
           JOptionPane.showMessageDialog(null, "Campo apellidos vacio!");
        }else{
          validados++;  
       }
       if(TFDireccionEdit.getText().trim().length()==0){
            JOptionPane.showMessageDialog(null, "Campo dirección vacio!");
       }else{
           validados++;
       }
       if(TFFechaNacimientoEdit.toString().length()==0){
            JOptionPane.showMessageDialog(null, "Campo fecha de nacimiento vacio!");
       }else{
           validados++;
       }
       if(TFTelefonoEdit.toString().length()==0){
            JOptionPane.showMessageDialog(null, "Campo télefono vacio!");
       }else{
           validados++;
       }
       if(CBEstadoCivilEdit.getSelectedItem()==SELECCIONE){
            JOptionPane.showMessageDialog(null, "Selecciona el estado civil!");
       }else{
           validados++;
       }
       if(CBTipoDocumentoEdit.getSelectedItem()==SELECCIONE){
            JOptionPane.showMessageDialog(null, "Selecciona el tipo de documento!");
       }else{
           validados++;
       }
       if(CBSexoEdit.getSelectedItem()==SELECCIONE){
            JOptionPane.showMessageDialog(null, "Selecciona el sexo!");
       }else{
           validados++;
       }
       return validados; 
    }
    
    private void TFDireccionEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFDireccionEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFDireccionEditActionPerformed

    private void TFCedulaEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFCedulaEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFCedulaEditActionPerformed

    private void TFFechaNacimientoEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFFechaNacimientoEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFFechaNacimientoEditActionPerformed

    private void CBTipoDocumentoEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBTipoDocumentoEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CBTipoDocumentoEditActionPerformed

    private void BtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarActionPerformed
        // TODO add your handling code here:
        mostrarDialogoConfirmarEliminar();
    }//GEN-LAST:event_BtnEliminarActionPerformed

    private void mostrarDialogoConfirmarEliminar(){
        String ced = JOptionPane.showInputDialog(null, "¿Escribe el número de identificacion del funcionario para eliminarlo.");
        if(TFCedulaEdit.getText().equals(ced)){
            eliminarFuncionario();
        }else{
            JOptionPane.showMessageDialog(null, "Identificación incorrecta!");
        }
    }
    
    private void eliminarFuncionario(){
        try {
            funcionariosController.EliminarTelefono(Integer.parseInt(TFCedulaEdit.getText()));
            int ced=Integer.parseInt(TFCedulaEdit.getText());
            funcionariosController.eliminarFuncionario(ced);
            JOptionPane.showMessageDialog(null, "Funcionario eliminado!");
            cargarFuncionarios();
            limpiarCamposEdit();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problemas al eliminar télefono de funcionario!");
        }
    }
    
    private void limpiarCamposEdit(){
           TFCedulaEdit.setText("");
           TFNombresEdit.setText("");
           TFApellidosEdit.setText("");
           TFDireccionEdit.setText("");
           TFFechaNacimientoEdit.setText("");
           TFTelefonoEdit.setText("");
           CBTipoDocumentoEdit.setSelectedIndex(0);
           CBSexoEdit.setSelectedIndex(0);
           CBEstadoCivilEdit.setSelectedIndex(0);
    }
    
    private int validarCamposVacios() {
        int validados=0;
       if(TFCedula.getText().trim().length()==0){
           JOptionPane.showMessageDialog(null, "Campo cédula vacio!");
        }else{
          validados++; 
       }
       if(TFNombres.getText().trim().length()==0){
           JOptionPane.showMessageDialog(null, "Campo nombres vacio!");
        }else{
           validados++;
       }
       if(TFApellidos.getText().trim().length()==0){
           JOptionPane.showMessageDialog(null, "Campo apellidos vacio!");
        }else{
          validados++;  
       }
       if(TFDireccion.getText().trim().length()==0){
            JOptionPane.showMessageDialog(null, "Campo dirección vacio!");
       }else{
           validados++;
       }
       if(TFFechaNacimiento.getText().length()==0){
            JOptionPane.showMessageDialog(null, "Campo fecha de nacimiento vacio!");
       }else{
           validados++;
       }
       
       if(TFTelefono.getText().length()==0){
            JOptionPane.showMessageDialog(null, "Campo télefono vacio!");
       }else{
           validados++;
       }
       
       if(CBTipoDocumento.getSelectedItem()==SELECCIONE){
            JOptionPane.showMessageDialog(null, "Selecciona el tipo de documento!");
       }else{
           validados++;
       }
        
       if(CBSexo.getSelectedItem()==SELECCIONE){
            JOptionPane.showMessageDialog(null, "Selecciona el sexo!");
       }else{
           validados++;
       }
       
       if(CBEstadoCivil.getSelectedItem()==SELECCIONE){
            JOptionPane.showMessageDialog(null, "Selecciona el estado civil!");
       }else{
           validados++;
       }
       return validados;
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCrear;
    private javax.swing.JButton BtnEditar;
    private javax.swing.JButton BtnEliminar;
    private javax.swing.JComboBox<String> CBEstadoCivil;
    private javax.swing.JComboBox<String> CBEstadoCivilEdit;
    private javax.swing.JComboBox<Funcionario> CBFuncionarios;
    private javax.swing.JComboBox<String> CBSexo;
    private javax.swing.JComboBox<String> CBSexoEdit;
    private javax.swing.JComboBox<String> CBTipoDocumento;
    private javax.swing.JComboBox<String> CBTipoDocumentoEdit;
    private javax.swing.JLabel LabelApellidos;
    private javax.swing.JLabel LabelApellidos2;
    private javax.swing.JLabel LabelCedula;
    private javax.swing.JLabel LabelCedula2;
    private javax.swing.JLabel LabelDireccion;
    private javax.swing.JLabel LabelDireccion2;
    private javax.swing.JLabel LabelEstadoCivil;
    private javax.swing.JLabel LabelEstadoCivil2;
    private javax.swing.JLabel LabelFechaNacimiento;
    private javax.swing.JLabel LabelFechaNacimiento2;
    private javax.swing.JLabel LabelLoading;
    private javax.swing.JLabel LabelNombre;
    private javax.swing.JLabel LabelNombre2;
    private javax.swing.JLabel LabelSexo;
    private javax.swing.JLabel LabelSexo2;
    private javax.swing.JLabel LabelTelefono;
    private javax.swing.JLabel LabelTelefono2;
    private javax.swing.JLabel LabelTipoDocumento;
    private javax.swing.JLabel LabelTipoDocumento2;
    private javax.swing.JTabbedPane PanelCrear;
    private javax.swing.JPanel PanelEditar;
    private javax.swing.JTextField TFApellidos;
    private javax.swing.JTextField TFApellidosEdit;
    private javax.swing.JTextField TFCedula;
    private javax.swing.JTextField TFCedulaEdit;
    private javax.swing.JTextField TFDireccion;
    private javax.swing.JTextField TFDireccionEdit;
    private javax.swing.JTextField TFFechaNacimiento;
    private javax.swing.JTextField TFFechaNacimientoEdit;
    private javax.swing.JTextField TFNombres;
    private javax.swing.JTextField TFNombresEdit;
    private javax.swing.JTextField TFTelefono;
    private javax.swing.JTextField TFTelefonoEdit;
    private javax.swing.JTable TableFuncionarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPFuncionario;
    private javax.swing.JPanel jPFuncionarios1;
    private javax.swing.JPanel jPFuncionarios3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
