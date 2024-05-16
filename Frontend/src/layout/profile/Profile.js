import classNames from "classnames/bind";
import { useLocation } from "react-router-dom";
import styles from "./Profile.module.scss";
import {BiLogoFacebookCircle} from "react-icons/bi";
import {BiLogoGmail} from "react-icons/bi";
import {AiFillInstagram} from "react-icons/ai";
import {PiTiktokLogoFill} from "react-icons/pi";

import Image from "../../img/R.jfif";
import Button from "../../components/button/Button";

const cx = classNames.bind(styles);

function Profile() {
    const location = useLocation();
    return (
        <div className={cx('ctn')}>
            <div className={cx('container')}>
                <div className={cx('content')}>
                    <div className={cx('row')}>
                        <div className={cx('col-md-8')}>
                            <div className={cx('nav-left')}>
                                <div className={cx('nav-header')}>
                                    My Profile
                                </div>

                                <div className={cx('nav-body')}>
                                    <div className={cx('row')}>
                                        <div className={cx('pr-md-1 col-md-5')}>
                                            <div className={cx('form-group')}>
                                                <label>University</label>
                                                <input disabled placeholder="College" type="text" 
                                                    className={cx('form-control-disabled')} 
                                                    value="PTIT"
                                                />
                                            </div>
                                        </div>
                                        <div className={cx('pr-md-1 col-md-3')}>
                                            <div className={cx('form-group')}>
                                                <label>Username</label>
                                                <input placeholder="Username" type="text"
                                                    value="badliar0812" className={cx('form-control')}
                                                />
                                            </div>
                                        </div>
                                        <div className={cx('pr-md-1 col-md-4')}>
                                            <div className={cx('form-group')}>
                                                <label>Email address</label>
                                                <input placeholder="Email" type="text"
                                                    value="buiquangtuan0812@gmail.com" className={cx('form-control')}
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div className={cx('row')}>
                                        <div className={cx('pr-md-1 col-md-6')}>
                                            <div className={cx('form-group')}>
                                                <label>First Name</label>
                                                <input placeholder="First Name" type="text" 
                                                    className={cx('form-control')}
                                                    value="Bad"
                                                />
                                            </div>
                                        </div>
                                        <div className={cx('pr-md-1 col-md-6')}>
                                            <div className={cx('form-group')}>
                                                <label>Last Name</label>
                                                <input placeholder="Last Name" type="text" 
                                                    className={cx('form-control')}
                                                    value="Liar"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div className={cx('row')}>
                                        <div className={cx('col-md-12')}>
                                            <div className={cx('form-group')}>
                                                <label>Address</label>
                                                <input placeholder="Address" type="text" 
                                                    className={cx('form-control')}
                                                    value="43 Dai Mo - Nam Tu Liem - Ha Noi"
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div className={cx('row')}>
                                        <div className={cx('pr-md-1 col-md-4')}>
                                            <div className={cx('form-group')}>
                                                <label>City</label>
                                                <input placeholder="Address" type="text" 
                                                    className={cx('form-control')}
                                                    value="Ha Noi"
                                                />
                                            </div>
                                        </div>

                                        <div className={cx('pr-md-1 col-md-4')}>
                                            <div className={cx('form-group')}>
                                                <label>Country</label>
                                                <input placeholder="Address" type="text" 
                                                    className={cx('form-control')}
                                                    value="Vietnam"
                                                />
                                            </div>
                                        </div>

                                        <div className={cx('pr-md-1 col-md-4')}>
                                            <div className={cx('form-group')}>
                                                <label>Post Ccode</label>
                                                <input placeholder="Address" type="text" 
                                                    className={cx('form-control-disabled')}
                                                    value="10000" disabled
                                                />
                                            </div>
                                        </div>
                                    </div>

                                    <div className={cx('row')}>
                                        <div className={cx('col-md-8')}>
                                            <div className={cx('form-group')}>
                                                <label>About Me</label>
                                                <textarea placeholder="About Me" type="text" 
                                                    className={cx('form-control')}
                                                ></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div className={cx('nav-footer')}>
                                    <button>Save</button>
                                    <Button props = {location.state}/>
                                </div>
                            </div>
                        </div>
                        <div className={cx('col-md-4')}>
                            <div className={cx('nav-right')}>
                                <div className={cx('author')}>
                                    <div className={cx('block-one')}></div>
                                    <div className={cx('block-two')}></div>
                                    <div className={cx('block-three')}></div>
                                    <div className={cx('block-four')}></div>

                                    <img src = {Image} alt = "Avatar"/>
                                </div>

                                <div className={cx('information')}>
                                    <h5>Bad Liar</h5>
                                    <span>Intern Deverloper</span>
                                    <p>The best way to overcome fear is to get started!</p>
                                </div>

                                <div className={cx('social-link')}>
                                    <div className={cx('facebook')}>
                                        <BiLogoFacebookCircle className={cx('icon')}/>
                                    </div>

                                    <div className={cx('gmail')}>
                                        <BiLogoGmail className={cx('icon')}/>
                                    </div>

                                    <div className={cx('instagram')}>
                                        <AiFillInstagram className={cx('icon')}/>
                                    </div>

                                    <div className={cx('tiktok')}>
                                        <PiTiktokLogoFill className={cx('icon')}/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;
