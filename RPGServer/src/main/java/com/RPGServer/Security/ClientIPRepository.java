package com.RPGServer.Security;

import com.RPGServer.QRCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ClientIPRepository extends CrudRepository<ClientIP, Integer>
{
    ClientIP findByIP(String IP);
}
