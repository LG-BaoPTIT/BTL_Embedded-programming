import classNames from "classnames/bind";
import styles from "./Humidity.module.scss";

import {WiHumidity} from "react-icons/wi";

const cx = classNames.bind(styles);

function Humidity({humidity}) {
    return (
        <div className={cx('container')}>
            <div className={cx('description')}>
                <p className={cx('title')}>Độ ẩm</p>
                <p className={cx('humidity')}>{humidity}%</p>
            </div>
            <div className={cx('img-des')}>
                <WiHumidity className={cx('icon')}/>
            </div>
        </div>
    );
}


export default Humidity;