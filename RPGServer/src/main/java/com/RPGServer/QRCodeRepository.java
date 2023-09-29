package com.RPGServer;

import org.springframework.data.repository.CrudRepository;

public interface QRCodeRepository extends CrudRepository<QRCodeEntity, Integer>
{
	QRCodeEntity findByUUID(UUID uuid);
}
