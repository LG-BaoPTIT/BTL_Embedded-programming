import axios from "axios";

export const setHeader = (token) => {
	axios.defaults.headers.common["Authorization"] = `${token}`;
};

export const login = async (value) => {
	try {
		const response = await axios.post("http://localhost:8080/api/v1/auth/signin", value);
		localStorage.setItem("token", response.data.token);
		return response;
	} catch (error) {
		return error;
	}
};