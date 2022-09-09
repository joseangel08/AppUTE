'use strict';


module.exports = (sequelize, DataTypes) => {
    const persona = sequelize.define('persona', {
        identificacion: {type: DataTypes.STRING(15), unique: true, allowNull: false, defaultValue:"No DATA"},
        tipo_identificacion: {type: DataTypes.ENUM(['CEDULA', 'PASAPORTE']), defaultValue:"CEDULA"},
        apellidos: {type: DataTypes.STRING(75), defaultValue:"No DATA"},
        nombres: {type: DataTypes.STRING(75), defaultValue:"No DATA"},
        direccion: {type: DataTypes.STRING(250), defaultValue:"No DATA"},
        external_id: {type: DataTypes.UUID},
        celular: {type: DataTypes.STRING(15), defaultValue:"No DATA"},
    }, {freezeTableName: true});
    persona.associate = function(models){
        persona.hasOne(models.cuenta, {foreignKey: 'id_persona', as : 'cuenta'});
    };
    return persona;
};