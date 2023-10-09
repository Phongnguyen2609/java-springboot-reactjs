import React, { useEffect, useState } from 'react';
import "./sidebar.css";
import { Layout, Menu } from 'antd';
import { useHistory, useLocation } from "react-router-dom";
import { UserOutlined, ContainerOutlined, DashboardOutlined, BarsOutlined, HomeOutlined, ShoppingOutlined, AuditOutlined, ShoppingCartOutlined, FormOutlined, NotificationOutlined } from '@ant-design/icons';

const { SubMenu } = Menu;
const { Sider } = Layout;

function Sidebar() {

  const history = useHistory();
  const location = useLocation();
  const [user, setUser] = useState([]);

  const menuSidebarAdmin = [
    // {
    //   key: "dash-board",
    //   title: "Trang chủ",
    //   link: "/dash-board",
    //   icon: <DashboardOutlined />
    // },
    // {
    //   key: "account-management",
    //   title: "Sinh viên",
    //   link: "/account-management",
    //   icon: <UserOutlined />
    // },
    {
      key: "class-list",
      title: "Lớp học",
      link: "/class-list",
      icon: <HomeOutlined />
    },
    // {
    //   key: "department-list",
    //   title: "Khoa",
    //   link: "/department-list",
    //   icon: <FormOutlined />
    // },
    // {
    //   key: "summary-list",
    //   title: "Tổng kết",
    //   link: "/summary-list",
    //   icon: <ShoppingOutlined />
    // },
  ];

  const navigate = (link, key) => {
    history.push(link);
  }

  useEffect(() => {
      const user = localStorage.getItem("user");
      if(user){
        setUser(user);
      }
  },[])

  return (
    <Sider
      className={'ant-layout-sider-trigger'}
      width={215}
      style={{
        position: "fixed",
        top: 60,
        height: '100%',
        left: 0,
        padding: 0,
        zIndex: 1,
        marginTop: 0,
        boxShadow: " 0 1px 4px -1px rgb(0 0 0 / 15%)"
      }}
    >
     <Menu
          mode="inline"
          selectedKeys={location.pathname.split("/")}
          defaultOpenKeys={['account']}
          style={{ height: '100%', borderRight: 0, backgroundColor: "#FFFFFF" }}
          theme='light'
        >
          <>
            {
              menuSidebarAdmin.map((map) => (
                <Menu.Item
                  onClick={() => navigate(map.link, map.key)}
                  key={map.key}
                  icon={map.icon}
                  className="customeClass"
                >
                  {map.title}
                </Menu.Item>
              )) 
            }
          </>
          {/* < SubMenu key="sub1" icon={<InsertRowBelowOutlined />} title="Event Censorship">
            {menuSubEventAdmin.map((map) => (
              <Menu.Item
                onClick={() => navigate(map.link, map.key)}
                key={map.key}
                icon={map.icon}
                className="customeClass"
              >
                {map.title}
              </Menu.Item>
            ))}
          </SubMenu>

          < SubMenu key="sub2" icon={<ProfileOutlined />} title="Event Management">
            {menuSubEventStudentAdmin.map((map) => (
              <Menu.Item
                onClick={() => navigate(map.link, map.key)}
                key={map.key}
                icon={map.icon}
                className="customeClass"
              >
                {map.title}
              </Menu.Item>
            ))}
          </SubMenu> */}
        </Menu>

    </Sider >
  );
}

export default Sidebar;