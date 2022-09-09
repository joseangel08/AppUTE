'use strict'
var models = require('../models')
var persona = models.persona
var uuid = require('uuid')
class PersonaController {
  guardar(req, res) {
    if (
      req.body.identificacion &&
      req.body.identificacion != '' &&
      req.body.tipo_identificacion &&
      req.body.tipo_identificacion != '' &&
      req.body.apellidos &&
      req.body.apellidos != '' &&
      req.body.nombres &&
      req.body.nombres != '' &&
      req.body.correo &&
      req.body.correo != '' &&
      req.body.clave &&
      req.body.clave != '' &&
      req.body.direccion &&
      req.body.direccion != '' &&
      req.body.celular &&
      req.body.celular != ''
    ) {
      persona
        .findOne({ where: { identificacion: req.body.identificacion } })
        .then(function (person) {
          if (person) {
            res.json({ msg: 'Usuario ya ingresado con esta cedula', code: '404' })
          } else {
            var data = {
              identificacion: req.body.identificacion,
              tipo_identificacion: req.body.tipo_identificacion,
              apellidos: req.body.apellidos,
              nombres: req.body.nombres,
              direccion: req.body.direccion,
              celular: req.body.celular,
              external_id: uuid.v4(),
              cuenta: {
                correo: req.body.correo,
                clave: req.body.clave,
                external_id: uuid.v4(),
                estado: true,
              },
            }
            persona
              .create(data, { include: [{ model: models.cuenta, as: 'cuenta' }] })
              .then(function (newP) {
                if (newP) {
                  res.json({ msg: 'Creacion correcta', code: '200' })
                } else {
                  res.json({ msg: 'No se ha creado el recurso', code: '404' })
                }
              })
          }
        })
    } else {
      res.json({ msg: 'No se ha creado el recurso, campos reuqeridos!', code: '404' })
    }
  }
  listar(req, res) {
    persona
      .findAll({ attributes: { exclude: ['id', 'updatedAt', 'createdAt'] } })
      .then(function (persons) {
        res.json({ msg: 'Operacioncion correcta', code: '200', data: persons })
      })
  }
}
module.exports = PersonaController
