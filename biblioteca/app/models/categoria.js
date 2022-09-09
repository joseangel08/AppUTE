'use strict';


module.exports = (sequelize, DataTypes) => {
    const categoria = sequelize.define('categoria', {        
        nombres: {type: DataTypes.STRING(75), defaultValue:"No DATA"},        
        external_id: {type: DataTypes.UUID},
        estado: {type: DataTypes.BOOLEAN, defaultValue:true},
    }, {freezeTableName: true});
    categoria.associate = function(models){
        categoria.hasMany(models.producto, {foreignKey: 'id_categoria', as : 'categoria'});
    };
    return categoria;
};