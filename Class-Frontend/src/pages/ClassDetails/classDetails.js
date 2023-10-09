import React, { useState, useEffect } from 'react';
import "./classDetails.css";
import {
    Spin, Typography, Breadcrumb, Tabs, BackTop, Modal, Button, Tag, Form, Input, notification, Table, Popconfirm, Row, Col, message, Select, Dropdown, Space, Menu, Upload, Card, DatePicker
} from 'antd';
import {
    HomeOutlined, FormOutlined, PlusOutlined, EditOutlined, DeleteOutlined, UploadOutlined, MoreOutlined
} from '@ant-design/icons';
import axiosClient from '../../apis/axiosClient';
import { useParams, useHistory } from 'react-router-dom';
import TextArea from 'antd/lib/input/TextArea';
const { Option } = Select;

const { Title } = Typography;
const { TabPane } = Tabs;

const ClassDetails = () => {
    const [loading, setLoading] = useState(true);
    const [classroomData, setClassroomData] = useState(null);
    const [studentWithTeacher, setStudentWithTeacher] = useState(null);
    const [teacherWithClass, setTeacherWithClass] = useState(null);
    const [exercise, setExercise] = useState([]);
    const [point, setPoint] = useState([]);

    const [isModalVisible, setIsModalVisible] = useState(false);
    const [accountId, setAccountId] = useState('');
    const [username, setUsername] = useState('');
    const [awaitingData, setAwaitingData] = useState(null);
    const [page, setPage] = useState(1);
    const [options, setOptions] = useState([]);
    const [selectedUsername, setSelectedUsername] = useState('');
    const [visible, setVisible] = useState(false);
    const [visible2, setVisible2] = useState(false);

    const { id } = useParams();
    const [form] = Form.useForm();
    const [form2] = Form.useForm();
    const user = localStorage.getItem('user');
    const history = useHistory();

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        axiosClient.post(`/classroom/add-student-classroom?account=${teacherWithClass}&classroom=${id}`, { username: selectedUsername })
            .then((response) => {
                // Xử lý khi API trả về thành công (nếu cần)
                if (response.msg === "this is not the teacher in the class") {
                    return notification.error({ message: "Đây không phải là giáo viên trong lớp" })
                }
                notification.success({ message: 'Thêm sinh viên thành công' });
                fetchClassroomData();
                setIsModalVisible(false);
                setAccountId('');
                setUsername('');

            })
            .catch((error) => {
                // Xử lý khi API trả về lỗi (nếu cần)
                notification.error({ message: 'Thêm sinh viên thất bại' });
            });
    };

    const handleCancel = () => {
        setIsModalVisible(false);
        setAccountId('');
    };

    const handleMenuClick = (e) => {
        if (e.key === 'documents') {
            setVisible(true); // Hiển thị modal khi chọn option "Tài liệu"
        }
    };

    const handleExerciseClick = (exerciseId) => {
        history.push(`/exercise-details/${exerciseId}/${id}`);
    };

    const handleModalOk = async () => {
        try {
            const values = await form.validateFields();
            const accountId = localStorage.getItem('id');
            const { title, file, description } = values;

            const formData = new FormData();
            formData.append('title', title);
            formData.append('classroom', id);
            formData.append('accountId', accountId);

            // formData.append('description', description);

            if (file && file[0]) {
                formData.append('file', file[0].originFileObj);
            }

            // Gọi API /exercise/create-exercise-document ở đây và truyền formData
            const response = await axiosClient.post('/exercise/create-exercise-document', formData);

            if (response) {
                // Show success message using Ant Design message
                message.success('Upload tài liệu thành công');
                fetchClassroomData();
            } else {
                message.error('Đã xảy ra lỗi khi tạo tài liệu!');
            }

            setVisible(false); // Ẩn modal sau khi thực hiện thành công
            form.resetFields(); // Reset các trường trong form

        } catch (error) {
            // Xử lý lỗi nếu cần thiết
            message.error('Đã xảy ra lỗi khi tạo tài liệu!');
        }
    };
    const handleModalCancel = () => {
        setVisible(false); // Ẩn modal khi nhấn nút "Cancel"
    };

    const menu = (
        <Menu onClick={handleMenuClick}>
            <Menu.Item key="exercises">Bài tập</Menu.Item>
            <Menu.Item key="documents">Tài liệu</Menu.Item>
        </Menu>
    );

    const fetchClassroomData = async () => {
        try {

            const accountId = localStorage.getItem('id');

            const response = await axiosClient.get('/classroom/get-classroom?classroom=' + id);
            console.log(response);
            setClassroomData(response);

            const studentExercise = await axiosClient.get('/student/student-list-exercise', {
                params: { account: accountId, classroom: id },
            })

            setPoint(studentExercise);

            await axiosClient.get('/classroom/get-students-classroom', {
                params: { classroom: id },
            }).then((response) => {
                console.log(response);
                setStudentWithTeacher(response);
                setTeacherWithClass(response?.teacher?.teacherId)
            });

            await axiosClient.get('/classroom/get-attend-classroom-awaiting', {
                params: { classroom: id, account: accountId },
            })
                .then((response) => {
                    console.log(response);
                    setAwaitingData(response);
                })
                .catch((error) => {
                    console.error('Failed to fetch awaiting data:', error);
                });

            const exerciseResponse = await axiosClient.get('/classroom/get-exercise-to-classroom', {
                params: { classroom: id },
            });
            console.log("=====EXERCISE=====");
            console.log(exerciseResponse);
            setExercise(exerciseResponse.exercises);

            setLoading(false);
        } catch (error) {
            console.error('Failed to fetch classroom data:', error);
            setLoading(false);
        }
    };

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'index',
            render: (value, item, index) => (
                (page - 1) * 10 + (index + 1)
            ),
        },
        {
            title: 'Mã sinh viên',
            dataIndex: 'studentId',
            key: 'studentId',
        },
        {
            title: 'Họ tên',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'Action',
            key: 'action',
            render: (text, record) => (
                <div>
                    <Row>
                        <div className='groupButton'>
                            <Button
                                size="small"
                                icon={<EditOutlined />}
                                style={{ width: 150, borderRadius: 15, height: 30, marginTop: 5 }}
                                onClick={() => handleApproveClick(record)}

                            >{"Phê duyệt"}
                            </Button>
                            {/* <div
                                style={{ marginTop: 5 }}>
                                <Popconfirm
                                    title="Bạn có chắc chắn xóa phần này?"
                                    // onConfirm={() => handleDeleteCategory(record.maSv)}
                                    okText="Yes"
                                    cancelText="No"
                                >
                                    <Button
                                        size="small"
                                        icon={<DeleteOutlined />}
                                        style={{ width: 150, borderRadius: 15, height: 30 }}
                                    >{"Xóa"}
                                    </Button>
                                </Popconfirm>
                            </div> */}
                        </div>
                    </Row>
                </div >
            ),
        },
    ];

    const columns2 = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'index',
            render: (value, item, index) => (
                (page - 1) * 10 + (index + 1)
            ),
        },
        {
            title: 'Tên bài tập',
            dataIndex: 'exercise_name',
            key: 'exercise_name',
        },
        {
            title: 'Hạn cuối',
            dataIndex: 'deadline',
            key: 'deadline',
        },
        {
            title: 'Số điểm',
            dataIndex: 'point',
            key: 'point',
        },
        {
            title: 'Action',
            key: 'action',
            render: (text, record) => (
                <div>
                    <Row>
                        <div className='groupButton'>
                            {record.status === "complete" ?
                                <Tag color="blue" style={{ width: 80, textAlign: "center" }}>Đã nộp</Tag>
                                :
                                <Tag color="magenta" style={{ width: 80, textAlign: "center" }}>Chưa nộp</Tag>
                            }
                        </div>
                    </Row>
                </div >
            ),
        },
    ];


    const handleApproveClick = async (record) => {
        const accountUser = localStorage.getItem('id');

        const payload = {
            "students": [
                {
                    "studentId": record?.studentId
                }
            ]
        };
        try {
            const response = await axiosClient.post('/classroom/confirm-attend-classroom', payload, {
                params: {
                    account: accountUser,
                    classroom: id,
                }
            }).then((res) => {
                if (res.msg === "approved student for the class") {

                    // Show success message using Ant Design message
                    message.success('Phê duyệt thành công!');
                    fetchClassroomData();

                }
            });

            // Handle successful response here
            console.log(response);

        } catch (error) {
            // Handle error here
            console.error('Failed to approve classroom:', error);

            // Show error message using Ant Design message
            message.error('Đã xảy ra lỗi khi phê duyệt!');
        }
    };

    const handleCreateClick = () => {
        setVisible(true); // Hiển thị dropdown menu khi người dùng bấm vào nút "Tạo"
    };

    const handleSearch = async (value) => {
        try {
            const response = await axiosClient.get(`/classroom/search-student?query=${value}`);
            console.log(response);
            setOptions(response.students);
        } catch (error) {
            console.error('Failed to fetch students:', error);
        }
    };

    const handleLeaveClassroom = async (studentId) => {
        const accountUser = localStorage.getItem('id');

        try {
            const response = await axiosClient.delete(`/classroom/delete-student-from-classroom`, {
                params: {
                    account: accountUser,
                    classroom: id,
                    student: studentId,
                },
            });
            fetchClassroomData();

            console.log('Successfully left the classroom:', response);
        } catch (error) {
            console.error('Failed to leave the classroom:', error);
        }
    };

    const handleDeleteExercise = async (exercise) => {
        const accountUser = localStorage.getItem('id');

        console.log(exercise);

        try {
            const response = await axiosClient.delete(`/teacher/delete-exercise`, {
                params: {
                    account: accountUser,
                    classroom: id,
                    exercise: exercise.exerciseId,
                },
            });
            fetchClassroomData();

            console.log('Successfully delete exercise:', response);
        } catch (error) {
            console.error('Failed to delete exercise:', error);
        }
    };

    const handleCreateExercise = () => {
        setVisible2(true);
    }

    const handleCancel2 = () => {
        setVisible2(false);
    };

    const onFinish = async () => {
        try {

            const values = await form2.validateFields();
            const accountId = localStorage.getItem('id');
            const { title, file2, description, point, deadline } = values;

            const formData = new FormData();
            formData.append('accountId', accountId);
            formData.append('classroom', id);
            formData.append('title', title);
            formData.append('description', description);
            formData.append('point', point);
            formData.append('deadline', deadline.format('YYYY-MM-DD'));

            if (file2 && file2[0]) {
                formData.append('file', file2[0].originFileObj);
            }

            const response = await axiosClient.post('/exercise/create-exercise-attachment', formData);

            console.log(response.data);
            if (response.msg === "upload exercise successully") {
                message.success('Tạo bài tập thành công');
                fetchClassroomData();
                form2.resetFields(); // Reset các trường trong form
            } else {
                message.error('Đã xảy ra lỗi khi tạo bài tập!');
            }
            setVisible2(false);
        } catch (error) {
            // Xử lý lỗi nếu có
            console.error('Error:', error);

        }
    };

    useEffect(() => {

        fetchClassroomData();
    }, []);

    return (
        <div>
            <Spin spinning={loading}>
                <div className='detail-box'>
                    <div style={{ marginTop: 20 }}>
                        <Breadcrumb>
                            <Breadcrumb.Item href="">
                                <HomeOutlined />
                            </Breadcrumb.Item>
                            <Breadcrumb.Item href="">
                                <FormOutlined />
                                <span>Chi tiết lớp học</span>
                            </Breadcrumb.Item>
                        </Breadcrumb>
                    </div>

                    <Tabs defaultActiveKey="1" style={{ marginTop: 20 }}>
                        <TabPane tab="Bảng tin" key="1">
                            <div
                                style={{
                                    backgroundImage: `url("https://gstatic.com/classroom/themes/img_cooking.jpg")`,
                                    width: '100%',
                                    height: '400px',
                                    backgroundSize: 'cover',
                                    backgroundPosition: 'center',
                                    backgroundRepeat: 'no-repeat',
                                    position: 'relative',
                                }}
                            >
                                <div
                                    style={{
                                        position: 'absolute',
                                        bottom: '10px',
                                        left: '10px',
                                        textAlign: 'left',
                                        color: 'white',
                                        textShadow: '2px 2px 4px rgba(0, 0, 0, 0.5)',
                                        fontSize: '24px',
                                        color: 'white',
                                    }}
                                >
                                    <h1 style={{ color: "white" }}>{classroomData?.classroomName}</h1>
                                    <h3 style={{ color: "white" }}>Mã lớp: {classroomData?.code}</h3>
                                </div>
                            </div>

                        </TabPane>
                        <TabPane tab="Bài tập trên lớp" key="2">
                            {/* Nội dung cho tab Bài tập trên lớp */}
                            {studentWithTeacher?.teacher?.username === user && (
                                <Space>
                                    <Dropdown
                                        overlay={
                                            <Menu>
                                                <Menu.Item onClick={handleCreateClick}>Tạo tài liệu</Menu.Item>
                                                <Menu.Item onClick={handleCreateExercise}>Tạo bài tập</Menu.Item>
                                            </Menu>
                                        }
                                        trigger={['click']}
                                    >
                                        <Button type="primary" icon={<PlusOutlined />} style={{ margin: 0 }}>
                                            Tạo
                                        </Button>
                                    </Dropdown>
                                </Space>)}

                            <div style={{ marginTop: '16px' }}>
                                {exercise?.map((exercise) => (
                                    <Card style={{ marginTop: '16px' }} key={exercise?.exerciseId} hoverable onClick={() => handleExerciseClick(exercise?.exerciseId)}>
                                        <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                                            <div style={{ display: 'flex', flexDirection: 'column' }}>
                                                <span style={{ fontSize: '18px', fontWeight: 'bold', color: '#333', marginBottom: '8px' }}>
                                                    {exercise?.title}
                                                </span>
                                                <span>Tên file: {exercise?.file}</span>
                                                <span>Loại file: {exercise?.type}</span>
                                            </div>
                                            {studentWithTeacher?.teacher?.username === user && (
                                                <div style={{ marginRight: 20 }}>
                                                    <Dropdown
                                                        overlay={
                                                            <Menu>
                                                                <Menu.Item onClick={() => handleDeleteExercise(exercise)}>
                                                                    Xóa
                                                                </Menu.Item>
                                                            </Menu>
                                                        }
                                                        trigger={['click']}
                                                    >
                                                        <Button
                                                            type="primary"
                                                            icon={<MoreOutlined />}
                                                            style={{ margin: 0 }}
                                                        />
                                                    </Dropdown>
                                                </div>
                                            )}
                                        </div>
                                    </Card>
                                ))}
                            </div>
                        </TabPane>

                        <TabPane tab="Mọi người" key="3">
                            <div>
                                <h2>Giáo viên</h2>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 10 }}>
                                    <div style={{ display: 'flex', alignItems: 'center' }}>
                                        <img
                                            src="https://lh3.googleusercontent.com/a/default-user=s32-c"
                                            alt="Teacher Avatar"
                                            style={{ width: '32px', height: '32px', marginRight: '8px', borderRadius: '50%', }}
                                        />
                                        {studentWithTeacher?.teacher?.username}
                                    </div>

                                </div>
                                <div>
                                </div>
                                <hr />
                                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                    <h2>Sinh viên</h2>
                                    {studentWithTeacher?.teacher?.username === user && (
                                        <Button style={{ marginTop: 10 }} type="primary" icon={<PlusOutlined />} onClick={showModal}>
                                            Thêm sinh viên
                                        </Button>
                                    )}
                                </div>
                                {studentWithTeacher?.students?.map((student, index) => (
                                    <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', marginBottom: 15 }}>
                                            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                                <div style={{ width: 300 }}>
                                                    <img
                                                        src="https://lh3.googleusercontent.com/a/default-user=s32-c"
                                                        alt="Teacher Avatar"
                                                        style={{ width: '32px', height: '32px', marginRight: '8px', borderRadius: '50%', }}
                                                    />
                                                    {student.username}
                                                </div>
                                                {studentWithTeacher?.teacher?.username === user && (
                                                    <div style={{ marginLeft: 50, width: 300 }}>
                                                        <Dropdown
                                                            overlay={
                                                                <Menu>
                                                                    <Menu.Item onClick={() => handleLeaveClassroom(student.studentId)}>
                                                                        Xóa khỏi lớp
                                                                    </Menu.Item>
                                                                </Menu>
                                                            }
                                                            trigger={['click']}
                                                        >
                                                            <Button
                                                                type="primary"
                                                                icon={<MoreOutlined />}
                                                                style={{ margin: 0 }}
                                                            />
                                                        </Dropdown>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </TabPane>

                        <TabPane tab="Phê duyệt học sinh" key="4">
                            <Table
                                dataSource={awaitingData?.students}
                                columns={columns}
                                rowKey="studentId"
                            />
                        </TabPane>
                        {studentWithTeacher?.teacher?.username !== user && (
                            <TabPane tab="Điểm" key="5">
                                <Table
                                    dataSource={point?.exercises}
                                    columns={columns2}
                                    pagination={false}
                                    rowKey="studentId"
                                />
                            </TabPane>)}
                    </Tabs>

                </div>

                <Modal
                    title="Thêm sinh viên vào lớp học"
                    visible={isModalVisible}
                    onOk={handleOk}
                    onCancel={handleCancel}
                >
                    <Form>
                        <Form.Item label="Nhập Tên">
                            <Select
                                showSearch
                                value={selectedUsername}
                                onSearch={handleSearch}
                                onChange={(value) => setSelectedUsername(value)}
                                style={{ width: '100%' }}
                            >
                                {options.map((option) => (
                                    <Option key={option.studentId} value={option.username}>
                                        {option.username}
                                    </Option>
                                ))}
                            </Select>
                        </Form.Item>
                    </Form>
                </Modal>

                <Modal
                    title="Tạo tài liệu"
                    visible={visible}
                    onOk={handleModalOk}
                    onCancel={handleModalCancel}
                >
                    <Form form={form}>
                        <Form.Item
                            label="Tiêu đề"
                            name="title"
                            rules={[{ required: true, message: 'Vui lòng nhập tiêu đề tài liệu!' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item
                            label="Mô tả"
                            name="description"
                            rules={[{ required: true, message: 'Vui lòng nhập mô tả!' }]}
                        >
                            <TextArea />
                        </Form.Item>
                        <Form.Item
                            label="File"
                            name="file"
                            valuePropName="fileList"
                            getValueFromEvent={(e) => e && e.fileList}
                            rules={[{ required: true, message: 'Vui lòng tải lên tệp!' }]}
                        >
                            <Upload name="file" customRequest={() => { }}>
                                <Button icon={<UploadOutlined />}>Tải lên</Button>
                            </Upload>
                        </Form.Item>
                    </Form>
                </Modal>

                <Modal
                    title="Tạo bài tập"
                    visible={visible2}
                    onCancel={handleCancel2}
                    onOk={onFinish}
                >
                    <Form form={form2}>
                        <Form.Item name="title" label="Tiêu đề" rules={[{ required: true }]}>
                            <Input />
                        </Form.Item>
                        <Form.Item name="description" label="Mô tả" rules={[{ required: true }]}>
                            <Input.TextArea />
                        </Form.Item>
                        <Form.Item name="point" label="Số điểm" rules={[{ required: true }]}>
                            <Input type="number" />
                        </Form.Item>
                        <Form.Item name="deadline" label="Hạn nộp" rules={[{ required: true }]}>
                            <DatePicker />
                        </Form.Item>
                        <Form.Item
                            label="File"
                            name="file2"
                            valuePropName="fileList"
                            getValueFromEvent={(e) => e && e.fileList}
                            rules={[{ required: true, message: 'Vui lòng tải lên tệp!' }]}
                        >
                            <Upload name="file2" customRequest={() => { }}>
                                <Button icon={<UploadOutlined />}>Tải lên</Button>
                            </Upload>
                        </Form.Item>
                    </Form>
                </Modal>
                <BackTop style={{ textAlign: 'right' }} />
            </Spin>
        </div >
    )
}

export default ClassDetails;
