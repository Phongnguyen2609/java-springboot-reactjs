import axiosClient from './axiosClient';

const productApi = {
    /*Danh sách api category */

    createCategory(data) {
        const url = '/category/search';
        return axiosClient.post(url, data);
    },
    getToursList(){
        const url = '/Khoas/';
        return axiosClient.get(url);
    },
    getClassList(){
        const id = localStorage.getItem("id");
        const url = '/classroom/get-classroom-to-account?account='+id;
        return axiosClient.get(url);
    },
    getDetailCategory(id) {
        const url = '/category/' + id;
        return axiosClient.get(url);
    },
    getListCategory(data) {
        const url = '/category/search';
        if(!data.page || !data.limit){
            data.limit = 10;
            data.page = 1;
        }
        return axiosClient.post(url,data);
    },
    deleteCategory(id) {
        const url = "/category/" + id;
        return axiosClient.delete(url);
    },
    searchCategory(name) {
        const params = {
            name: name.target.value
        }
        const url = '/category/searchByName';
        return axiosClient.get(url, { params });
    },

    /*Danh sách api product */

    createProduct(data) {
        const url = '/product/search';
        return axiosClient.post(url, data);
    },
    getDetailProduct(id) {
        const url = '/Khoas/' + id;
        return axiosClient.get(url);
    },
    getDetailBangtongkets(id) {
        const url = '/Bangtongkets/' + id;
        return axiosClient.get(url);
    },
    getListProducts(data) {
        const url = '/Bangtongkets';
        return axiosClient.get(url);
    },
    deleteProduct(id) {
        const url = "/Lops/" + id;
        return axiosClient.delete(url);
    },
    deleteKhoas(id) {
        const url = "/Khoas/" + id;
        return axiosClient.delete(url);
    },
   
    uploadImage() {
        const url = "/upload/uploadfile";
        return axiosClient.post(url);
    },
    searchProduct(name) {
        const params = {
            name: name.target.value
        }
        const url = '/hotels/searchByName';
        return axiosClient.get(url, { params });
    },
    approvalHotel(id, visible){
        const url = "/hotels/" + id;
        return axiosClient.put(url, { visible });
    }
}

export default productApi;