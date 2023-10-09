import axiosClient from "./axiosClient";

const userApi = {
    login(username, password) {
        const url = '/auth/login';
        return axiosClient
            .post(url, {
                username,
                password,
            })
            .then(response => {
                console.log(response);
                if (response) {
                    localStorage.setItem("token", response.jwt);
                    localStorage.setItem("user", response.username);
                    localStorage.setItem("id", response.id);

                }
                return response;
            });
    },
    logout(data) {
        const url = '/users/logout';
        return axiosClient.get(url);
    },
    listUserByAdmin(data) {
        const url = '/Sinhviens';
        return axiosClient.get(url);
    },
    banAccount(data, id) {
        const url = '/users/' + id;
        return axiosClient.put(url, data);
    },
    unBanAccount(data, id) {
        const url = '/users/' + id;
        return axiosClient.put(url, data);
    },
    getProfile() {
        const url = '/users/profile';
        return axiosClient.get(url);
    },
    searchUser(email) {
        console.log(email);
        const params = {
            email: email.target.value
        }
        const url = '/users/searchByEmail';
        return axiosClient.get(url, { params });
    },
}

export default userApi;