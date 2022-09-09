'use strict';


module.exports = (sequelize, DataTypes) => {
    const producto = sequelize.define('producto', {        
        nombre: {type: DataTypes.STRING(75), defaultValue:"No DATA"},
        p_compra: {type: DataTypes.DOUBLE, defaultValue:"0.0"},
        p_venta: {type: DataTypes.DOUBLE, defaultValue:"0.0"},
        estado: {type: DataTypes.BOOLEAN, defaultValue:true},
        external_id: {type: DataTypes.UUID}
        
    }, {freezeTableName: true});
    producto.associate = function(models){
        producto.belongsTo(models.categoria, {foreignKey: 'id_categoria'});
    };
    return producto;
};