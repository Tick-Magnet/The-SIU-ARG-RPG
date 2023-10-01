package com.RPGServer;

import org.springframework.data.repository.CrudRepository;
import java.util.UUID;


public interface QRCodeRepository extends CrudRepository<QRCodeEntity, Integer>
{
	QRCodeEntity findByuuid(UUID uuid);
}
