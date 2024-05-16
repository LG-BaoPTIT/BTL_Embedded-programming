import classNames from "classnames/bind";
import styles from "./Button.module.scss";
import {AiTwotoneHome} from "react-icons/ai";
import { Link } from "react-router-dom";

const cx = classNames.bind(styles);

function Button({props}) {
    return (
        <Link to = "/" state = {props}>
            <div className={cx('btn-back')}>
                <AiTwotoneHome className={cx('icon-home')}/>
            </div>
        </Link>
    );
}

export default Button;