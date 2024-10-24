
import {useEffect, useState} from "react";
import {Link, NavLink, useLocation} from "react-router-dom";
import events from "../pages/Events.jsx";
import member from "../pages/Member.jsx";

const navItems = [
  { path: '/', label: '행사관리', icon: '/src/assets/images/ico_event_w.svg' },
  { path: '/member', label: '회원관리', icon: '/src/assets/images/ico_member_w.svg' },
];

function Navigation() {
  const [activeLink, setActiveLink] = useState(localStorage.getItem('activeLink') || '/');

  useEffect(() => {
    localStorage.setItem('activeLink', activeLink);
  }, [activeLink]);


  // 로고 클릭 시 호출되는 함수
  const handleLogoClick = () => {
    setActiveLink('/'); // 첫 번째 메뉴 선택
    localStorage.removeItem('activeLink'); // 로컬 스토리지 해제
  };


  return (
    <div className={'nav flex-column bg-point vh-100 nav-style'}>
      <p className={'text-center pt-4'}>
        <Link to={'/'} className={'fw-bold text-white fs-4'} onClick={handleLogoClick}>
          <img src="/logo_w.svg" alt="한국IT협회 로고" style={{width:'60%'}}/></Link>
      </p>

      <nav>
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={`nav-item ${activeLink === item.path ? 'active' : ''}`}
            onClick={() => setActiveLink(item.path)}
          >
            <img
              src={item.icon}
              alt={item.label}
              className="nav-icon"
            />
            <span className="nav-label">{item.label}</span>
          </NavLink>
        ))}
      </nav>

      <p className={'ft-txt'}>Copyright © <br/>CheckManager. All rights reserved.</p>
    </div>
  )
}

export default Navigation;