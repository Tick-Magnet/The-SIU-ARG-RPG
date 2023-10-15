import axios from 'axios'
import Cookies from 'universal-cookie';

const apiURL = 'http://localhost:8080';
//axios.defaults.headers.get['Acess-Control-Allow-Origin'] = '*';
const cookies = new Cookies();

class RPGServerProxy
{
	
	checkToken(inputUsername)
	{
		var url ="";
		url = url.concat(apiURL, "/checktoken");
		axios.post(url,
		{
			username: inputUsername,
			token: cookies.get('sessionToken')
		})
		.then(function (response)
		{
			console.log(response.data);
		});
	}
	
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
		//this.checkToken(inputUsername);
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
			cookies.set('sessionToken', response.data.token, {path:'/', maxAge:82800});
			console.log(response.data);
			return response.data;
		});
	}
}

export default new RPGServerProxy();
