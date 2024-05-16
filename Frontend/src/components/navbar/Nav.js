import classNames from "classnames/bind";
import { Link } from "react-router-dom";
import styles from "./Nav.module.scss";

import { AiTwotoneHome } from "react-icons/ai";
import { GrHistory } from "react-icons/gr";
import { RiLogoutBoxLine } from "react-icons/ri";


const cx = classNames.bind(styles);

function Nav({props}) {
    return (
        <div className={cx('container__nav')}>
            <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="rgba(255, 255, 255, 1)"
                viewBox="0 0 24 8"
                width="1em"
                height="1em"
                className={cx('tiktok-znnspw-StyledTopArrow')}
            >
                <path d="M0 8c7 0 10-8 12-8s5 8 12 8z"></path>
            </svg>
            <div className={cx('container__item')}>
                <Link to = "/" state={props}>
                    <div className={cx('nav-item')}>
                        <div>
                            <AiTwotoneHome className={cx('icon')}/>
                            Home
                        </div>
                    </div>
                </Link>

                <Link to = "/action-history" state={props}>
                    <div className={cx('nav-item')}>
                        <div>
                            <GrHistory className={cx('icon')}/>
                            History
                        </div>
                    </div>
                </Link>

                <Link to = "/login" reloadDocument state={props} onClick={() => {
                        localStorage.removeItem('token')
                        localStorage.removeItem('home_id')
                    }}>
                    <div className={cx('nav-item')} >
                        <div>
                            <RiLogoutBoxLine className={cx('icon')}/>
                            Log out
                        </div>
                    </div>
                </Link>

            </div>
        </div>
    );
}

export default Nav;