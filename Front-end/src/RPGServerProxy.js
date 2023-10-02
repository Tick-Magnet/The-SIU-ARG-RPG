import axios from 'axios'

const apiURL = 'http://localhost:8080';
//axios.defaults.headers.get['Acess-Control-Allow-Origin'] = '*';

class RPGServerProxy
{
	
	testAPI()
	{
		var url = "";
		//url.concat(apiURL, "/test");
		axios.get(apiURL).then(function (response)
		{
			console.log(response.data);
			console.log(response.status);	
		});
		return "test"
	}
	
	register(inputUsername, inputEmail, inputPassword)
	{
		var url = "";
		url = url.concat(apiURL, "/register");
		console.log(url);
		axios.post(url,
		{
			username: inputUsername,
			email: inputEmail,
			password: inputPassword
		})
		.then(function (response)
		{
			console.log(response.data);
			return response.data;
		});
	}
	
	login(inputUsername, inputPassword)
	{
		var url = "";
		url = url.concat(apiURL, "/login");
		axios.post(url,
		{
			username: inputUsername,
			password: inputPassword
		})
		.then(function (response)
		{
			//Store session token as a cookie
			var cookieString = "SessionToken=";
			cookieString = cookieString.concat(response.data.token);
			//Cookie expires in 23 hours
			cookieString = cookieString.concat(";SameSite=None;max-age=82800");
			document.cookie = cookieString;
			console.log(response.data);
			return response.data;
		});
	}
}

export default new RPGServerProxy();
