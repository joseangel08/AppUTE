'use strict'
var models = require('../models')
var producto = models.producto
var categoria = models.categoria
var uuid = require('uuid')
class ProductoController {
  guardar(req, res) {
    if (
      req.body.nombre &&
      req.body.nombre != '' &&
      req.body.p_compra &&
      req.body.p_compra != '' &&
      req.body.p_venta &&
      req.body.p_venta != '' &&
      req.body.external_cat &&
      req.body.external_cat != ''
    ) {
      producto.findOne({ where: { nombre: req.body.nombre } }).then(function (person) {
        if (person) {
          res.json({
            msg: 'Producto ya ingresado con este nombre',
            code: '404',
          })
        } else {
          categoria
            .findOne({ where: { external_id: req.body.external_cat } })
            .then(function (cat) {
              if (cat) {
                var data = {
                  nombre: req.body.nombre,
                  p_compra: req.body.p_compra,
                  p_venta: req.body.p_venta,
                  external_id: uuid.v4(),
                  estado: true,
                  id_categoria: cat.id,
                }
                producto.create(data).then(function (newP) {
                  if (newP) {
                    res.json({ msg: 'Creacion correcta', code: '200' })
                  } else {
                    res.json({
                      msg: 'No se ha creado el recurso',
                      code: '404',
                    })
                  }
                })
              } else {
                res.json({
                  msg: 'No se ha creado el recurso, categoria no existe!',
                  code: '404',
                })
              }
            })
        }
      })
    } else {
      res.json({
        msg: 'No se ha creado el recurso, campos reuqeridos!',
        code: '404',
      })
    }
  }
  listar(req, res) {
    producto
      .findAll({ attributes: { exclude: ['id', 'updatedAt', 'createdAt'] } })
      .then(function (persons) {
        res.json({ msg: 'Operacioncion correcta', code: '200', data: persons })
      })
  }
}
module.exports = ProductoController
