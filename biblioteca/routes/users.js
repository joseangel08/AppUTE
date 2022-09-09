var express = require('express')
var router = express.Router()

/* GET users listing. */
router.get('/', function (req, res, next) {
  res.send('respond with a resource')
})

router.get('/privado', function (req, res, next) {
  var models = require('./../app/models')
  //{ force: true }
  models.sequelize
    .sync()
    .then(() => {
      console.log('Se ha conectado la bd')
      res.send('Se ha sincronizado la bd')
    })
    .catch(err => {
      console.log(err, 'Hubo un error')
      res.send('No se pudo sincronizar bd')
    })
})

module.exports = router
