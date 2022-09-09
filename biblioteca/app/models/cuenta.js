'use strict';


module.exports = (sequelize, DataTypes) => {
    const cuenta = sequelize.define('cuenta', {
        correo: {type: DataTypes.STRING(100), unique: true, allowNull: false, defaultValue:"No DATA"},
        
        clave: {type: DataTypes.STRING(75), defaultValue:"No DATA"},
        estado: {type: DataTypes.BOOLEAN, defaultValue:true},
        external_id: {type: DataTypes.UUID}
        
    }, {freezeTableName: true});
    cuenta.associate = function(models){
        cuenta.belongsTo(models.persona, {foreignKey: 'id_persona'});
    };
    return cuenta;
};