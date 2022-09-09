'use strict';
var models = require('../models');
var categoria = models.categoria;
var uuid = require('uuid');
class CategoriaController {
    guardar(req, res) {
        if(req.body.nombres && req.body.nombres != "") {
            categoria.findOne({where: {nombres: req.body.nombres}}).then(function(obj){
                if(obj) {
                    res.json({msg:"Categoria ya guardada", code: "404"});
                } else {
                    var data = {
                        nombres: req.body.nombres,                       
                        external_id: uuid.v4(),
                        estado: true
                        
                    };
                    categoria.create(data).then(function(newP){
                        if(newP) {
                            res.json({msg:"Creacion correcta", code: "200"});
                        } else {
                            res.json({msg:"No se ha creado el recurso", code: "404"});
                        }
                    });
                }
            });
        } else {
            res.json({msg:"No se ha creado el recurso, campos reuqeridos!", code: "404"});
        }
    }
    listar(req, res) {
        categoria.findAll({attributes: {exclude: ['id','updatedAt','createdAt']},}).then(function(persons){
            res.json({msg:"Operacioncion correcta", code: "200", data: persons});
        });
    }
}
module.exports = CategoriaController;