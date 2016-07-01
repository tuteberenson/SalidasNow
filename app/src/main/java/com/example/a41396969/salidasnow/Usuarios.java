package com.example.a41396969.salidasnow;

/**
 * Created by berenson on 30/06/2016.
 */
public class Usuarios {
    private String _Username, _Nombre,_Apellido,_NombreImg, _Password;
    private int _idUsuario;

    public String get_Username()
    {
        return _Username;
    }

    public void set_Username(String Username)
    {
     _Username=Username;
    }

    public String get_Password() {
        return _Password;
    }

    public void set_Password(String pass) {
        _Password = pass;
    }

    public String get_Nombre() {
        return _Nombre;
    }

    public void set_Nombre(String _Nombre) {
        this._Nombre = _Nombre;
    }

    public String get_Apellido() {
        return _Apellido;
    }

    public void set_Apellido(String _Apellido) {
        this._Apellido = _Apellido;
    }

    public String get_NombreImg() {
        return _NombreImg;
    }

    public void set_NombreImg(String _NombreImg) {
        this._NombreImg = _NombreImg;
    }

    public int get_idUsuario() {
        return _idUsuario;
    }

    public void set_idUsuario(int _idUsuario) {
        this._idUsuario = _idUsuario;
    }
}
