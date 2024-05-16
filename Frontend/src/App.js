import Home from "./layout/home/Home";
import Profile from "./layout/profile/Profile";
import Datasensor from "./layout/data_sensor/DataSensor";
import History from "./layout/action_history/Action";
import { Route, Routes, Navigate } from "react-router-dom";
import { useEffect, useState } from "react";
import Login from "./layout/login/Login";
import { useJwt } from 'react-jwt'
import { setHeader } from "./api";

function App() {
  const token = localStorage.getItem("token")
  const { isExpired } = useJwt(token)
  const [user, setUser] = useState(!!token)

  useEffect(() => {
    if (token && !isExpired) {
      setUser(true)
      setHeader(token)
    } else {
      setUser(false)
    }
  }, [isExpired, token]);

  return (
    <Routes>
      {!user ? <>
        <Route path='/login' element={<Login setUser={setUser} />} />
        <Route path='*' element={<Navigate to={'/login'} replace />}/>
      </> : <> 
        <Route exact index element={<Home/>} ></Route>
        <Route path = "profile" element = {<Profile/>}></Route>
        <Route path = "action-history" element = {<History/>}></Route>
        <Route path='*' element={<Navigate to={'/'} />} replace/>
      </>}
    </Routes>
  );
}

export default App;
