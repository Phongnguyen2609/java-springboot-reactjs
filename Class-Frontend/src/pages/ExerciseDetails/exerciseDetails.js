import React, { useState, useEffect } from 'react';
import "./exerciseDetails.css";
import {
    Spin, Typography, Breadcrumb, Tabs, BackTop, Modal, Button, Icon, Divider, Form, Input, notification, Table, Popconfirm, Row, Col, message, Select, Dropdown, Space, Menu, Upload, Card, DatePicker
} from 'antd';
import {
    HomeOutlined, FormOutlined, PlusOutlined, EditOutlined, DeleteOutlined, TeamOutlined, ContainerOutlined, SendOutlined, UploadOutlined, MoreOutlined
} from '@ant-design/icons';
import axiosClient from '../../apis/axiosClient';
import { useParams } from 'react-router-dom';
import TextArea from 'antd/lib/input/TextArea';
const { Option } = Select;

const { Title } = Typography;
const { TabPane } = Tabs;

const ExerciseDetails = () => {
    const [loading, setLoading] = useState(true);
    const [classroomData, setClassroomData] = useState(null);
    const [studentWithTeacher, setStudentWithTeacher] = useState(null);
    const [teacherWithClass, setTeacherWithClass] = useState(null);
    const [exercise, setExercise] = useState([]);

    const [page, setPage] = useState(1);
    const [visible2, setVisible2] = useState(false);
    const [commentData, setCommentData] = useState();
    const [myExercise, setMyExercise] = useState();

    const { id } = useParams();
    const { exerciseId } = useParams();
    const { classroomId } = useParams();

    const [form] = Form.useForm();
    const [form2] = Form.useForm();
    const user = localStorage.getItem('user');


    const fetchClassroomData = async () => {
        try {

            const accountId = localStorage.getItem('id');

            const response = await axiosClient.get('/classroom/get-classroom?classroom=' + classroomId);
            console.log(response);
            setClassroomData(response);

            const studentExercise = await axiosClient.get('/student/student-list-exercise', {
                params: { account: accountId, classroom: classroomId },
            })

            console.log(exerciseId);
            const targetExerciseId = exerciseId;

            const targetExercise = studentExercise.exercises.find(exercise => exercise.exerciseId == targetExerciseId);
            console.log(targetExercise);
            setMyExercise(targetExercise);


            const comment = await axiosClient.get('/comment/get-all-comment-to exercise?exercise=' + exerciseId);
            console.log(comment);
            setCommentData(comment);

            await axiosClient.get('/classroom/get-students-classroom', {
                params: { classroom: classroomId },
            }).then((response) => {
                console.log(response);
                setStudentWithTeacher(response);
                setTeacherWithClass(response?.teacher?.teacherId)
            });

            await axiosClient.get('/teacher/get-all-account-post-exercise', {
                params: { exercise: exerciseId, account: accountId, classroom: classroomId },
            })
                .then((response) => {
                    console.log(response);
                    setExercise(response);
                })
                .catch((error) => {
                    console.error('Failed to fetch awaiting data:', error);
                });



            setLoading(false);
        } catch (error) {
            console.error('Failed to fetch classroom data:', error);
            setLoading(false);
        }
    };

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
            const { file2 } = values;

            const formData = new FormData();
            formData.append('account', accountId);
            formData.append('classroom', classroomId);
            formData.append('exercise', exerciseId);

            if (file2 && file2[0]) {
                formData.append('file', file2[0].originFileObj);
            }

            const response = await axiosClient.post('/student/account-post-exercise', formData);

            console.log(response.data);
            if (response.msg === "student post exercise successfully") {
                message.success('Nộp bài tập thành công');
                fetchClassroomData();
                form2.resetFields(); // Reset các trường trong form
            } else {
                message.error('Đã xảy ra lỗi khi nộp bài tập!');
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

    const [inputValue, setInputValue] = useState('');
    const [studentId, setStudentId] = useState('');

    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    const handleInputBlur = async (studentId, value) => {
        console.log(studentId);
        if (value !== '') {
            // Call your API here, passing inputValue and student.studentId

            const accountUser = localStorage.getItem('id');
            const point = value;
            try {
                const response = await axiosClient.post(`/teacher/update-point?account=${accountUser}&classroom=${classroomId}&exercise=${exerciseId}&student=${studentId}`, {
                    point
                });
                fetchClassroomData();

                console.log('Successfully update point student:', response);
            } catch (error) {
                console.error('Failed to update point the classroom:', error);
            }
        }
    };

    const [showInput, setShowInput] = useState(false);
    const [comment, setComment] = useState('');

    const handleShowInput = () => {
        setShowInput(true);
    };

    const handleCommentChange = (e) => {
        setComment(e.target.value);
    };

    const handleSendComment = async () => {
        const accountUser = localStorage.getItem('id');

        if (comment.trim() !== '') {
            try {
                // Call your API to send the comment here
                const response = await axiosClient.post(`/comment/add-comment-to-exercise?account=${accountUser}&classroom=${classroomId}&exercise=${exerciseId}`, {
                    content: comment,
                });

                // Clear the comment input and any other necessary actions
                setComment('');
                fetchClassroomData();

                console.log('Comment sent successfully:', response);
            } catch (error) {
                console.error('Failed to send comment:', error);
            }
        }
    };

    const handleDeleteCommentStudent = async (commentId) => {
        const accountUser = localStorage.getItem('id');

        try {
            const response = await axiosClient.delete(`/comment/student-delete-comment-exercise`, {
                params: {
                    account: accountUser,
                    comment: commentId,
                    exercise: exerciseId,
                },
            });
            fetchClassroomData();

            console.log('Successfully delete comment the classroom:', response);
        } catch (error) {
            console.error('Failed to delete comment the classroom:', error);
        }
    }

    const handleDeleteCommentTeacher = async (commentId) => {
        const accountUser = localStorage.getItem('id');

        try {
            const response = await axiosClient.delete(`/comment/teacher-delete-comment-exercise`, {
                params: {
                    account: accountUser,
                    comment: commentId,
                    exercise: exerciseId,
                    classroom: classroomId
                },
            });
            fetchClassroomData();

            console.log('Successfully delete comment the classroom:', response);
        } catch (error) {
            console.error('Failed to delete comment the classroom:', error);
        }
    }



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
                                <span>Chi tiết bài tập</span>
                            </Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    {studentWithTeacher?.teacher?.username === user && (

                        <Tabs defaultActiveKey="1" style={{ marginTop: 20 }}>
                            <TabPane tab="Hướng dẫn" key="1">
                                <Card>
                                    <Row justify="space-between" align="middle">
                                        <Col style={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>

                                            <div style={{ display: 'flex', flexDirection: 'column' }}>
                                                <div>
                                                    <ContainerOutlined />
                                                    <span style={{ marginLeft: 8 }}>{classroomData?.classroomName} - {classroomData?.code}</span>
                                                </div>
                                                <div style={{ marginTop: 5 }}>
                                                    <strong>Giáo viên: </strong> {studentWithTeacher?.teacher?.username}
                                                </div>
                                            </div>
                                        </Col>
                                        <Divider />
                                    </Row>
                                    <Row justify="space-between" align="middle" style={{ marginTop: '10px' }}>
                                        <Col>
                                            <TeamOutlined />
                                            <span style={{ marginLeft: '10px' }}>Nhận xét của lớp học</span>
                                        </Col>
                                    </Row>
                                    <div style={{ marginTop: 20 }}>
                                        {commentData?.account_comments?.map((student, index) => (
                                            <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                                                <div style={{ display: 'flex', alignItems: 'center', marginBottom: 15 }}>
                                                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                                        <div style={{ width: 300, display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                                                            <img
                                                                src="https://lh3.googleusercontent.com/a/default-user=s32-c"
                                                                alt="Teacher Avatar"
                                                                style={{ width: '32px', height: '32px', marginRight: '8px', borderRadius: '50%', }}
                                                            />
                                                            <div>
                                                                <strong>{student.student.username}</strong>
                                                                <div>
                                                                    {student.comment.content}
                                                                </div>
                                                                <div style={{ fontSize: 12 }}>
                                                                    {student.comment_date}
                                                                </div>
                                                            </div>
                                                        </div>

                                                        {student?.student?.username === user && (
                                                            <div style={{ marginLeft: 50, width: 300 }}>
                                                                <Dropdown
                                                                    overlay={
                                                                        <Menu>
                                                                            <Menu.Item onClick={() => handleDeleteCommentTeacher(student.comment.commentId)}>
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
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                    <div>
                                        {showInput ? (
                                            <div style={{ display: 'flex', marginTop: 10 }}>
                                                <Input.TextArea
                                                    placeholder="Nhập nhận xét..."
                                                    value={comment}
                                                    onChange={handleCommentChange}
                                                />
                                                <Button
                                                    type="primary"
                                                    icon={<SendOutlined />}
                                                    style={{ marginLeft: '10px' }}
                                                    onClick={handleSendComment}
                                                >
                                                    Gửi
                                                </Button>
                                            </div>
                                        ) : (
                                            <div style={{ marginTop: '10px' }}>
                                                <span style={{ cursor: 'pointer' }} onClick={handleShowInput}>
                                                    Thêm nhận xét về lớp học
                                                </span>
                                            </div>
                                        )}
                                    </div>
                                </Card>
                            </TabPane>

                            <TabPane tab="Bài tập của học viên" key="2">
                                <div>
                                    <Row gutter={16} style={{ marginTop: 15 }}>
                                        {/* Cột bên trái */}
                                        <Col span={14}>
                                            <Card>
                                                <Row justify="space-between" align="middle" style={{ marginBottom: '16px', fontSize: 18, fontWeight: 600 }}>
                                                    <Col>
                                                        <TeamOutlined />
                                                        <span style={{ marginLeft: '10px' }}>Tất cả học viên</span>
                                                    </Col>
                                                </Row>
                                                {exercise?.account_post_exercises?.map((student, index) => (
                                                    <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                                                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 15 }}>
                                                            <div style={{ display: 'flex', alignItems: 'center' }}>
                                                                <div style={{ width: 300 }}>
                                                                    <img
                                                                        src="https://lh3.googleusercontent.com/a/default-user=s32-c"
                                                                        alt="Teacher Avatar"
                                                                        style={{ width: '32px', height: '32px', marginRight: '8px', borderRadius: '50%', }}
                                                                    />
                                                                    {student.username}
                                                                </div>
                                                                {studentWithTeacher?.teacher?.username === user && (
                                                                    <div style={{ marginLeft: 20, width: 120 }}>
                                                                        <Input
                                                                            style={{ marginTop: 10 }}
                                                                            placeholder="Nhập số điểm"
                                                                            value={inputValue}
                                                                            onChange={handleInputChange}
                                                                            onBlur={() => {
                                                                                handleInputBlur(student.accountId, inputValue);
                                                                            }}
                                                                        />
                                                                    </div>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </div>
                                                ))}
                                            </Card>
                                        </Col>
                                        {/* Cột bên phải */}
                                        <Col span={10}>
                                            <Card>
                                                <h3 style={{ fontSize: 18, fontWeight: 600 }}>{exercise.title}</h3>

                                                <div style={{ display: "flex", flexDirection: 'row' }}>
                                                    <div>
                                                        <strong style={{ fontSize: 26, fontWeight: 600 }}>{exercise.number_of_students_submit}</strong>
                                                        <h3 style={{ fontSize: 14, fontWeight: 600 }}>Đã nộp</h3>
                                                    </div>
                                                    <div style={{ marginLeft: 40, borderLeft: '1px solid #ccc', padding: '0 20px' }}></div>

                                                    <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                                                        <strong style={{ fontSize: 26, fontWeight: 600 }}>{exercise.total_students}</strong>
                                                        <h3 style={{ fontSize: 14, fontWeight: 600 }}>Đã giao</h3>
                                                    </div>
                                                </div>
                                                {/* Các nội dung khác trong thẻ Card */}
                                            </Card>
                                        </Col>
                                    </Row>
                                </div>
                            </TabPane>
                        </Tabs>
                    )}

                    {studentWithTeacher?.teacher?.username !== user && (
                        <Row gutter={16} style={{ marginTop: 15 }}>
                            {/* Cột bên trái */}
                            <Col span={14}>
                                <Card>
                                    <Row justify="space-between" align="middle">
                                        <Col>
                                            <ContainerOutlined />
                                            <span style={{ marginLeft: '8px' }}>{classroomData?.classroomName} - {classroomData?.code}</span>
                                        </Col>
                                        <Divider />
                                    </Row>
                                    <Row justify="space-between" align="middle" style={{ marginTop: '10px' }}>
                                        <Col>
                                            <TeamOutlined />
                                            <span style={{ marginLeft: '10px' }}>Nhận xét của lớp học</span>
                                        </Col>
                                    </Row>
                                    <div style={{ marginTop: 20 }}>
                                        {commentData?.account_comments?.map((student, index) => (
                                            <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                                                <div style={{ display: 'flex', alignItems: 'center', marginBottom: 15 }}>
                                                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                                        <div style={{ width: 300, display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                                                            <img
                                                                src="https://lh3.googleusercontent.com/a/default-user=s32-c"
                                                                alt="Teacher Avatar"
                                                                style={{ width: '32px', height: '32px', marginRight: '8px', borderRadius: '50%', }}
                                                            />
                                                            <div>
                                                                <strong>{student.student.username}</strong>
                                                                <div>
                                                                    {student.comment.content}
                                                                </div>
                                                                <div style={{ fontSize: 12 }}>
                                                                    {student.comment_date}
                                                                </div>
                                                            </div>

                                                        </div>
                                                        {student?.student?.username === user && (
                                                            <div style={{ marginLeft: 50, width: 300 }}>
                                                                <Dropdown
                                                                    overlay={
                                                                        <Menu>
                                                                            <Menu.Item onClick={() => handleDeleteCommentStudent(student.comment.commentId)}>
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
                                                            </div>)}
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                    {showInput ? (
                                        <div style={{ display: 'flex', marginTop: 10 }}>
                                            <Input.TextArea
                                                placeholder="Nhập nhận xét..."
                                                value={comment}
                                                onChange={handleCommentChange}
                                            />
                                            <Button
                                                type="primary"
                                                icon={<SendOutlined />}
                                                style={{ marginLeft: '10px' }}
                                                onClick={handleSendComment}
                                            >
                                                Gửi
                                            </Button>
                                        </div>
                                    ) : (
                                        <div style={{ marginTop: '10px' }}>
                                            <span style={{ cursor: 'pointer' }} onClick={handleShowInput}>
                                                Thêm nhận xét về lớp học
                                            </span>
                                        </div>
                                    )}
                                </Card>
                            </Col>
                            {/* Cột bên phải */}
                            <Col span={10}>
                                <Card>
                                    <div>
                                        <h3>Bài tập của bạn: {myExercise?.exercise_name}</h3>
                                        <h3>Điểm của bạn: {myExercise?.point}</h3>
                                        <Button type="primary" icon={<SendOutlined />} onClick={handleCreateExercise} disabled={myExercise?.point}>{myExercise?.status === "complete" ? "Cập nhật" : "Nộp bài"}</Button>
                                    </div>
                                    {/* Các nội dung khác trong thẻ Card */}
                                </Card>
                            </Col>
                        </Row>
                    )}
                </div>

                <Modal
                    title="Nộp bài tập"
                    visible={visible2}
                    onCancel={handleCancel2}
                    onOk={onFinish}
                >
                    <Form form={form2}>
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

export default ExerciseDetails;
