var express = require('express');
var router = express.Router();
var personaControl = require('../app/controllers/personaController');
var PersonaController = new personaControl();

var categoriaControl = require('../app/controllers/categoriaController');
var CategoriaController = new categoriaControl();

var productoControl = require('../app/controllers/productoController');
var ProductoControl = new productoControl();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('API REST');
});
router.post('/persona/registro', PersonaController.guardar);
router.get('/persona', PersonaController.listar);

router.post('/categoria/registro', CategoriaController.guardar);
router.get('/categoria', CategoriaController.listar);

router.post('/producto/registro', ProductoControl.guardar);
router.get('/producto', ProductoControl.listar);

module.exports = router;
