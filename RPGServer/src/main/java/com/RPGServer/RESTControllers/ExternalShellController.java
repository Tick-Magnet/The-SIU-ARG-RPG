package com.RPGServer.RESTControllers;

import com.RPGServer.ShellCommands;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Shell;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.*;

@RestController
public class ExternalShellController
{
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ShellCommands shellCommands;

    @PostMapping("/shellCommand")
    public Map<String, Object> shellCommand(@RequestBody Map<String, Object> payload) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        HashMap<String,Object> output = new HashMap<String,Object>();
        String username = (String)payload.get("username");
        String token = (String)payload.get("token");
        String command = (String)payload.get("command");
        //Check that user exists
        UserAccount user = userAccountRepository.findByUsername(username);
        if(user != null)
        {
            //Validate session token
            if(token != null && user.isValidSessionToken(token))
            {
                //Validate user role
                if(user.userRole == UserAccount.UserRole.ADMIN)
                {
                    //Check that command is valid
                    Method[] methods = shellCommands.getClass().getMethods();
                    String[] tokens = command.split(" ");
                    boolean valid = false;
                    Method method = null;
                    for(int i = 0; i < methods.length; i++)
                    {
                        if(methods[i].getName().equals(tokens[0]))
                        {
                            valid = true;
                            method = methods[i];
                            break;
                        }
                    }
                    //Run command and return result
                    if(valid == true)
                    {
                       String returnMessage =(String) method.invoke(shellCommands, Arrays.copyOfRange(tokens,1,tokens.length));
                       output.put("result", returnMessage);
                    }
                }
            }
        }
        else
        {
            output.put("message", "Could not find user with that username");
        }
        return output;
    }
}
