/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peluchitos2;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author diego
 */
public class Peluchitos2 {

    /**
     * @param args the command line arguments
     */
    
    private Scanner teclado;
    private int o, i, k; 
    private boolean seguir, find, flag;
    private String nombre;
    private int cantidad;
    private int precio;
    private int ventas=0;
    private int ganancias=0;
    private int auxganancia=0;
    private int auxcant=0;
    private int auxvent=0;
    private int opc=0;
    public String url="jdbc:mysql://localhost/peluchitos";
    public String url2="jdbc:mysql://db4free.net/contactosbanol";
    public String user="root";
    public String user2="diegobanol";
    public String passworld="mas,.facil";
    public String passworld2="db4free00medias";
    public Statement estado;
    public ResultSet resultado;
    
    public void coneccion(){

        try{
            //Prueba de conexión
            System.out.println("Conectando a  base de datos");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url2, user2, passworld2);       
            System.out.println("Conexión exitosa....");           
            estado = con.createStatement();
        }catch(SQLException ex){
            System.out.println("Error de mysql");
        }catch (Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }
        
    }
    
    public void menu(){
        seguir=true;
        teclado= new Scanner(System.in);
        System.out.println("\nAdministración de productos");
        do{
            System.out.println("\n¿Que desea hacer?");
            System.out.println("1.Registro de nuevos productos");
            System.out.println("2.Buscar Producto");
            System.out.println("3.Eliminar Producto");
            System.out.println("4.Mostrar Inventario");
            System.out.println("5.Realizar Ventas");
            System.out.println("6.Mostrar Ganancias totales");
            System.out.println("7.Salir");
            o=teclado.nextInt();
            System.out.println(" ");
            this.seleccion();
        }while(seguir);
    }
    
    public void seleccion(){
        teclado= new Scanner(System.in);
        seguir=true;
        switch(o){
            case 1:
                this.AgregarProducto();
                System.out.println("Registrado exitosamente");
                this.continuar();
                break;
            case 2:            
                System.out.println("Ingrese el nombre del producto que desea buscar");
                nombre=teclado.nextLine();
                this.BuscarProducto();    
                if(find==false) System.out.println("No se encuentra el producto");
                this.continuar();
                break;
            case 3:
                System.out.println("Ingrese el nombre del producto que desea eliminar");
                nombre=teclado.nextLine();
                this.EliminarProducto();    
                if(find==false) System.out.println("No se encuentra el producto");
                this.continuar();
                break;
            case 4:
                this.MostrarInventario();
                this.continuar();
                break;
            case 5:
                System.out.println("Ingrese el nombre del producto que desea vender");
                nombre=teclado.nextLine();
                this.RealizarVentas();                      
                if(find==false) System.out.println("No se encuentra el producto");
                this.continuar();
                break;
            case 6:
                this.MostrarGanancias();
                this.continuar();
                break;
            case 7:
                seguir=false;
                break;
            default:
                System.out.println("\nOpcion incorrecta\n");
                break;
        }
    }
    
    public void AgregarProducto(){
        teclado= new Scanner(System.in);
        System.out.println("Ingrese el nombre del nuevo producto");
        nombre=teclado.nextLine();
        System.out.println("Cuanta cantidad hay de dicho producto");
        cantidad=teclado.nextInt();
        System.out.println("Cual es el precio del producto en pesos");
        precio=teclado.nextInt();
        try{
            estado.executeUpdate("INSERT INTO `productos` VALUES (NULL, '"+nombre+"', '"+cantidad+"', '"+precio+"', '0' )");
        }catch(SQLException ex){
            System.out.println("Error de mysql");
        }catch (Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }
         
    }
    
    public void BuscarProducto(){
        find=false;
        try{
            resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `nombre` LIKE '"+nombre+"' ");
            
            while(resultado.next()){
                System.out.println("Nombre: "+resultado.getString("nombre")+"\t"+ "Cantidad: "+resultado.getString("cantidad") +"\t"+ "Precio "+resultado.getString("precio"));
                find=true;
            }
            
        }catch(SQLException ex){
            System.out.println("Error de mysql");
        }catch (Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }      
    }
    
    public void EliminarProducto(){
        find=false;
        try{
            if(estado.executeUpdate("DELETE FROM `productos` where `nombre` LIKE '"+nombre+"' ")==1){
                find=true;
                System.out.println("Producto eliminado correctamente");
            }
        }catch(SQLException ex){
            System.out.println("Error de mysql");
        }catch (Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }
    }
    
    public void MostrarInventario(){
        try{
            resultado = estado.executeQuery("SELECT * FROM `productos`");          
            while(resultado.next()){
                System.out.println("Nombre: "+resultado.getString("nombre")+"\t"+ "Cantidad: "+resultado.getString("cantidad") +"\t"+ "Precio "+resultado.getString("precio")+"\n");
                find=true;
            }            
        }catch(SQLException ex){
            System.out.println("Error de mysql");
        }catch(Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }
    }
    
    public void RealizarVentas(){
        teclado= new Scanner(System.in);
        find=false;
        try{
            resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `nombre` LIKE '"+nombre+"' ");
            
            while(resultado.next()){
                find=true;
                System.out.println("Producto encontrado, cuantos vendera: ");
                ventas=teclado.nextInt();
                auxcant=Integer.parseInt(resultado.getString("cantidad"));
                auxvent=Integer.parseInt(resultado.getString("ventas"));
                if(auxcant-ventas < 0){
                    System.out.println("No hay suficiente ingrese otra cantidad");
                }else{                  
                    flag=true;                   
                }               
            }            
            if(flag){
                estado.executeUpdate("UPDATE `productos` SET `Ventas` = '"+(auxvent+ventas)+"' WHERE `Nombre` = '"+nombre+"'");
                estado.executeUpdate("UPDATE `productos` SET `Cantidad` = '"+(auxcant-ventas)+"' WHERE `Nombre` = '"+nombre+"'");
            }
        }catch(SQLException ex){
            System.out.println("Error de mysql");
            System.out.println(ex);
        }catch (Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }
    }
    
    public void MostrarGanancias(){
      
        try{
            resultado = estado.executeQuery("SELECT * FROM `productos`");
            
            while(resultado.next()){

                auxcant=Integer.parseInt(resultado.getString("cantidad"));
                auxvent=Integer.parseInt(resultado.getString("ventas"));
                ganancias=Integer.parseInt(resultado.getString("ventas"))*Integer.parseInt(resultado.getString("precio"));
                auxganancia=ganancias+auxganancia;
                System.out.println("Ventas de " +resultado.getString("nombre")+": " +auxvent+ "\tGanancia: $"+ganancias); 
                
            }
            
            System.out.println("Las ganancias totales son: "+auxganancia);
            auxganancia=0;

        }catch(SQLException ex){
            System.out.println("Error de mysql");
            System.out.println(ex);
        }catch (Exception e){
            System.out.println("Se ha encontrado un error de tipo " +e.getMessage());
        }       
    }
    
    public void continuar(){
        System.out.println(" ");        
        flag=true;
        teclado= new Scanner(System.in);
        do{
            System.out.println("Desea continuar? 1. Si / 2. No");
            opc=teclado.nextInt();
            if(opc==1 | opc==2){
                flag=false;
            }else{
                System.out.println("Ingrese una opción correcta");
            }
        }while(flag);
        
        if(opc==2) seguir=false;
        
    }
    
    
    
    public static void main(String[] args) {
        Peluchitos2 Productos=new Peluchitos2();
        Productos.coneccion();
        Productos.menu();
    }
}
