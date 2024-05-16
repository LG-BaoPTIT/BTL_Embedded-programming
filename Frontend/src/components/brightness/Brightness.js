import classNames from "classnames/bind";
import styles from "./Bright.module.scss";

import {AiFillCloud} from "react-icons/ai";
import {BsFillBrightnessHighFill, BsFillBrightnessAltLowFill} from "react-icons/bs";

const cx = classNames.bind(styles);

function Brightness({brightness}) {
    return (
        <div className={cx(brightness <= 28 ? 'container_item-cloud' : (brightness >= 60 ? 'container_item-hot' : 'container_item-sun'))}>
            <div className={cx('description')}>
                <p className={cx('title')}>Độ sáng</p>
                <p className={cx('bright')}>{brightness}lux</p>
            </div>

            <div className={cx('img-des')}>
                {
                    brightness <= 28 ?
                        <AiFillCloud className={cx('icon')}/>
                    : (
                        brightness >= 60 ? 
                        <BsFillBrightnessHighFill className={cx('icon')}/>
                        : 
                        <BsFillBrightnessAltLowFill className={cx('icon')}/>
                    )
                }
            </div>
        </div>
    );
}

export default Brightness;