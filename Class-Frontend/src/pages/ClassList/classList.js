import React, { useState, useEffect } from 'react';
import "./classList.css";
import {
    Col, Row, Typography, Spin, Button, PageHeader, Card, Drawer, Empty, Input, Space,
    Form, Pagination, Modal, Popconfirm, notification, BackTop, Tag, Breadcrumb, Select, Table, Menu, Dropdown, message,
} from 'antd';
import {
    FolderOutlined, BarChartOutlined, DeleteOutlined, PlusOutlined, EyeOutlined, SearchOutlined,
    CalendarOutlined, EllipsisOutlined, TeamOutlined, HomeOutlined, HistoryOutlined, FormOutlined, TagOutlined, EditOutlined
} from '@ant-design/icons';
import QRCode from 'qrcode.react';
import eventApi from "../../apis/eventApi";
import productApi from "../../apis/productsApi";
import { useHistory } from 'react-router-dom';
import { DateTime } from "../../utils/dateTime";
import axiosClient from '../../apis/axiosClient';
import 'suneditor/dist/css/suneditor.min.css';
import SunEditor from 'suneditor-react';

const { confirm } = Modal;
const { Option } = Select;
const { Title } = Typography;
const DATE_TIME_FORMAT = "DD/MM/YYYY HH:mm";

const ClassList = () => {
    const [product, setProduct] = useState([]);
    const [category, setCategoryList] = useState([]);
    const [openModalCreate, setOpenModalCreate] = useState(false);
    const [openModalUpdate, setOpenModalUpdate] = useState(false);
    const [image, setImage] = useState();

    const [open, setOpen] = useState(false);
    const [loading, setLoading] = useState(true);
    const [form] = Form.useForm();
    const [form2] = Form.useForm();
    const [isClicked, setIsClicked] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [description, setDescription] = useState();
    const [total, setTotalList] = useState(false);
    const [id, setId] = useState();
    const [visible, setVisible] = useState(false);
    const [user, setUser] = useState([]);
    const [selectedRecord, setSelectedRecord] = useState(null);

    const accountId = localStorage.getItem('id');
    const history = useHistory();

    const showModal = () => {
        setOpenModalCreate(true);
    };

    const handleOkUser = async (values) => {
        setLoading(true);
        try {
            var formData = new FormData();
            formData.append("image", image);
            await axiosClient.post("/uploadFile", formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(response => {
                const categoryList = {
                    "classroomName": values.classroomName,
                    "topic": values.topic,
                    "room": values.room,
                }
                const id = localStorage.getItem("id");

                return axiosClient.post("/classroom/create-classroom?account=" + id, categoryList).then(response => {
                    if (response === undefined) {
                        notification["error"]({
                            message: `Thông báo`,
                            description:
                                'Tạo lớp thất bại',
                        });
                    }
                    else {
                        notification["success"]({
                            message: `Thông báo`,
                            description:
                                'Tạo lớp thành công',
                        });
                        setOpenModalCreate(false);
                        handleProductList();
                    }
                })
            })

            setLoading(false);
        } catch (error) {
            throw error;
        }
    }

    const handleUpdateProduct = async (values) => {
        setLoading(true);
        try {
            const categoryList = {
                "maLop": values.maLop,
                "tenLop": values.tenLop,
            }
            return axiosClient.put("/Lops/" + id, categoryList).then(response => {
                if (response === undefined) {
                    notification["error"]({
                        message: `Thông báo`,
                        description:
                            'Chỉnh sửa lớp thất bại',
                    });
                    setLoading(false);
                } else {
                    notification["success"]({
                        message: `Thông báo`,
                        description:
                            'Chỉnh sửa lớp thành công',
                    });
                    setOpenModalUpdate(false);
                    handleProductList();
                    setLoading(false);
                }
            })
        } catch (error) {
            throw error;
        }
    }

    const handleCancel = (type) => {
        if (type === "create") {
            setOpenModalCreate(false);
        } else {
            setOpenModalUpdate(false)
        }
        console.log('Clicked cancel button');
    };

    const handleProductList = async () => {
        try {
            await productApi.getClassList().then((res) => {
                console.log(res);
                setProduct(res.classrooms);
                setLoading(false);
            });
            ;
        } catch (error) {
            console.log('Failed to fetch product list:' + error);
        };
    };

    const handleDeleteCategory = async (id) => {
        setLoading(true);
        try {
            await productApi.deleteProduct(id).then(response => {
                if (response === undefined) {
                    notification["error"]({
                        message: `Thông báo`,
                        description:
                            'Xóa lớp thất bại',

                    });
                    setLoading(false);
                }
                else {
                    notification["success"]({
                        message: `Thông báo`,
                        description:
                            'Xóa lớp thành công',

                    });
                    setCurrentPage(1);
                    handleProductList();
                    setLoading(false);
                }
            }
            );

        } catch (error) {
            console.log('Failed to fetch event list:' + error);
        }
    }

    const handleDetailView = (id) => {
        history.push("/product-detail/" + id)
    }

    const handleChangeImage = (event) => {
        setImage(event.target.files[0]);
    }


    const handleProductEdit = (id) => {
        setOpenModalUpdate(true);
        (async () => {
            try {
                const response = await productApi.getDetailProduct(id);
                console.log(response);
                setId(id);
                form2.setFieldsValue({
                    maLop: response.maLop,
                    tenLop: response.tenLop,
                });
                console.log(form2);
                setDescription(response.description);
                setLoading(false);
            } catch (error) {
                throw error;
            }
        })();
    }

    const handleFilter = async (name) => {
        try {
            const res = await productApi.searchProduct(name);
            setTotalList(res.totalDocs)
            setProduct(res.data.docs);
        } catch (error) {
            console.log('search to fetch category list:' + error);
        }
    }

    const handleOpen = () => {
        setVisible(true);
    };

    const handleClose = () => {
        form.resetFields();
        setVisible(false);
    };

    const handleSubmit = () => {
        form.validateFields().then((values) => {
            form.resetFields();
            handleOkUser(values);
            setVisible(false);
        });
    };

    const handleMenuClick = (e) => {
        if (e.key === 'createClass') {
            // Xử lý logic khi chọn "Tạo lớp"
            handleOpen();
        } else if (e.key === 'joinClass') {
            showModal2();

        }
    };

    const menu = (
        <Menu onClick={handleMenuClick}>
            <Menu.Item key="createClass">
                Tạo lớp
            </Menu.Item>
            <Menu.Item key="joinClass">
                Tham gia lớp
            </Menu.Item>
        </Menu>
    );

    const [isModalVisible2, setIsModalVisible2] = useState(false);
    const [classCode, setClassCode] = useState('');

    const showModal2 = () => {
        setIsModalVisible2(true);
    };

    const handleCancel2 = () => {
        setIsModalVisible2(false);
    };

    const handleJoinClass = () => {

        const requestData = {
            code: classCode,
        };

        axiosClient.post('/classroom/attend-classroom?account=' + accountId, requestData)
            .then((response) => {
                if (response) {
                    notification['success']({
                        message: 'Thông báo',
                        description: 'Tham gia lớp thành công. Vui lòng đợi duyệt',
                    });
                    setIsModalVisible2(false);
                    handleProductList();
                } else {
                    notification['error']({
                        message: 'Thông báo',
                        description: 'Tham gia lớp thất bại',
                    });
                }
            })
            .catch((error) => {
                // Xử lý lỗi nếu có
                notification['error']({
                    message: 'Lỗi',
                    description: 'Có lỗi xảy ra khi gọi API',
                });
            });
    };

    const handleDeleteClassroom = async () => {
        console.log(selectedRecord);
        try {
            await axiosClient.delete(`/student/delete-classroom?student=${accountId}&classroom=${selectedRecord.classroomId}`);
            // Perform any additional actions or update the UI as needed
            message.success('Rời lớp thành công.');
            handleProductList();

        } catch (error) {
            message.error('Rời lớp thất bại:', error);
        }
    };

    const menu2 = (
        <Menu>
            <Menu.Item key="edit">Chỉnh sửa</Menu.Item>
            <Menu.Item key="delete" onClick={handleDeleteClassroom}>
                Rời lớp
            </Menu.Item>
            <Menu.Item key="move">Di chuyển</Menu.Item>
            <Menu.Item key="archive">Lưu trữ</Menu.Item>
        </Menu>
    );

    const handleCardClick = (record) => {
        history.push(`/class-details/${record.classroomId}`);
    };


    useEffect(() => {
        (async () => {
            try {

                const user = localStorage.getItem("user");
                setUser(user);
                await productApi.getClassList().then((res) => {
                    console.log(res);
                    setProduct(res.classrooms);
                    setLoading(false);
                });

                await productApi.getListCategory({ page: 1, limit: 10000 }).then((res) => {
                    console.log(res);
                    setCategoryList(res.data.docs);
                    setLoading(false);
                });
                ;
            } catch (error) {
                console.log('Failed to fetch event list:' + error);
            }
        })();
    }, [])
    return (
        <div>
            <Spin spinning={loading}>
                <div className='container2'>
                    <div style={{ marginTop: 20 }}>
                        <Breadcrumb>
                            <Breadcrumb.Item href="">
                                <HomeOutlined />
                            </Breadcrumb.Item>
                            <Breadcrumb.Item href="">
                                <FormOutlined />
                                <span>Danh sách lớp học</span>
                            </Breadcrumb.Item>
                        </Breadcrumb>
                    </div>

                    <div style={{ marginTop: 20 }}>
                        <div id="my__event_container__list">
                            <PageHeader
                                subTitle=""
                                style={{ fontSize: 14 }}
                            >
                                <Row>
                                    <Col span="18">

                                    </Col>
                                    <Col span="6">
                                        <Row justify="end">
                                            <Space>
                                                <Dropdown overlay={menu}>
                                                    <Button icon={<PlusOutlined />} style={{ marginLeft: 10, width: 80 }}>
                                                    </Button>
                                                </Dropdown>
                                            </Space>
                                        </Row>
                                    </Col>
                                </Row>

                            </PageHeader>
                        </div>
                    </div>
                    <>
                        {
                            <div style={{ marginTop: 30 }}>
                                <Row gutter={[16, 16]} style={{ margin: 0 }}>
                                    {product.map((record) => (
                                        <Card hoverable
                                            key={record.code} style={{ width: 300, marginRight: 15 }} className='class'>
                                            <div
                                                style={{
                                                    backgroundImage: `url("https://gstatic.com/classroom/themes/img_cooking.jpg")`,
                                                    width: '100%',
                                                    height: '80px',
                                                    backgroundSize: 'cover',
                                                    backgroundPosition: 'center',
                                                    backgroundRepeat: 'no-repeat',
                                                    position: 'relative',
                                                }}
                                            >
                                                <div
                                                    style={{
                                                        position: 'absolute',
                                                        top: '8px',
                                                        left: '8px',
                                                        fontSize: '16px',
                                                        color: 'white',
                                                        fontWeight: 'bold',
                                                        textShadow: '2px 2px 4px rgba(0, 0, 0, 0.5)',
                                                    }}
                                                >
                                                    <div>{record.classroomName}</div>
                                                </div>
                                                <Dropdown overlay={menu2} trigger={['click']} placement="bottomRight">
                                                    <div
                                                        style={{
                                                            position: 'absolute',
                                                            top: '8px',
                                                            right: '8px',
                                                            fontSize: '20px',
                                                            color: 'white',
                                                            cursor: 'pointer',
                                                        }}
                                                    >
                                                        <EllipsisOutlined onClick={() => setSelectedRecord(record)} />
                                                    </div>
                                                </Dropdown>
                                            </div>
                                            <div style={{ marginTop: '100px' }}>

                                            </div>
                                            <hr style={{ marginTop: '10px', marginBottom: '10px', borderColor: 'rgba(0, 0, 0, 0.1)' }} />
                                            <div style={{ display: 'flex', justifyContent: 'end', padding: '20px' }}>
                                                <BarChartOutlined style={{ fontSize: '20px', marginRight: '10px' }} />
                                                <EyeOutlined style={{ fontSize: '20px' }} onClick={() => handleCardClick(record)} />
                                            </div>
                                        </Card>
                                    ))}
                                </Row>
                            </div>
                        }
                    </>

                </div>

                <Modal
                    title="Tạo lớp mới"
                    visible={visible}
                    onCancel={() => setVisible(false)}
                    onClose={() => setVisible(false)}
                    width={1000}
                    footer={
                        <div
                            style={{
                                textAlign: 'right',
                            }}
                        >
                            <Button onClick={() => setVisible(false)} style={{ marginRight: 8 }}>
                                Hủy
                            </Button>
                            <Button onClick={handleSubmit} type="primary">
                                Hoàn thành
                            </Button>
                        </div>
                    }
                >
                    <Form
                        form={form}
                        name="eventCreate"
                        layout="vertical"
                        initialValues={{
                            residence: ['zhejiang', 'hangzhou', 'xihu'],
                            prefix: '86',
                        }}
                        scrollToFirstError
                    >

                        <Form.Item
                            name="classroomName"
                            label="Tên lớp"
                            rules={[
                                {
                                    required: true,
                                    message: 'Vui lòng nhập tên lớp!',
                                },
                            ]}
                            style={{ marginBottom: 10 }}
                        >
                            <Input placeholder="Tên lớp" />
                        </Form.Item>


                        <Form.Item
                            name="room"
                            label="Tên phòng"
                            rules={[
                                {
                                    required: true,
                                    message: 'Vui lòng nhập tên phòng!',
                                },
                            ]}
                            style={{ marginBottom: 10 }}
                        >
                            <Input placeholder="Tên phòng" />
                        </Form.Item>

                        <Form.Item
                            name="topic"
                            label="Tên Topic"
                            rules={[
                                {
                                    required: true,
                                    message: 'Vui lòng nhập tên topic!',
                                },
                            ]}
                            style={{ marginBottom: 10 }}
                        >
                            <Input placeholder="Tên topic" />
                        </Form.Item>

                    </Form>
                </Modal>

                <Modal
                    title="Tham gia lớp học"
                    visible={isModalVisible2}
                    onCancel={handleCancel2}
                    onOk={handleJoinClass}
                >
                    <p>Nhập mã code:</p>
                    <Input value={classCode} onChange={(e) => setClassCode(e.target.value)} />
                </Modal>

                {/* <Pagination style={{ textAlign: "center", marginBottom: 20 }} current={currentPage} defaultCurrent={1} total={totalEvent} onChange={handlePage}></Pagination> */}
                <BackTop style={{ textAlign: 'right' }} />
            </Spin>
        </div >
    )
}

export default ClassList;