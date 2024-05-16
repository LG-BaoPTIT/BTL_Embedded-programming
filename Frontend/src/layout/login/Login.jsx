import axios from "axios"
import { useState } from "react"
import { login, setHeader } from "../../api"

const Login = ({ setUser }) => {
  const [userName, setUserName] = useState('')
  const [password, setPassword] = useState('')
  const [errMsg, setErrMsg] = useState('')

  const onSubmit = async (e) => {
    e.preventDefault();
    
    await login({userName, passWord: password})
      .then(res => {
        console.log(res)
        if(res.status === 200) {
          setErrMsg('')
          console.log(res)
          localStorage.setItem('home_id', res.data.deviceId)
          setHeader(res.data.token)
          setUser(true)
        } else {
          setErrMsg(res.response.data)
        }
      })
      .catch(e => console.log(e))
  }

  return (
    <div style={{height: '100vh', display: 'flex', marginTop: '150px', justifyContent: 'center'}}>
      <form style={{width: "450px"}}>
      
      <header style={{fontSize: '48px', color: '#0d6efd', fontWeight: 'bold', textAlign: 'center', marginBottom: '40px'}}>Login</header>
        <div class="form-outline mb-4">
          <input type="email" id="form2Example1" class="form-control" value={userName} onChange={e => setUserName(e.target.value)} />
          <label class="form-label" htmlFor="form2Example1">Username</label>
        </div>

        <div class="form-outline mb-2">
          <input type="password" id="form2Example2" class="form-control" value={password} onChange={e => setPassword(e.target.value)} />
          <label class="form-label" htmlFor="form2Example2">Password</label>
        </div>

        {
          !!errMsg && <div className="form-outline mb-4">
            <div class="invalid-feedback" style={{display: 'block'}}>{errMsg}</div>
          </div>
        }

        <div class="row mb-4">
          <div class="col d-flex justify-content-center">
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="form2Example31" />
              <label class="form-check-label" htmlFor="form2Example31"> Remember me </label>
            </div>
          </div>

          <div class="col">
            <a href="#!">Forgot password?</a>
          </div>
        </div>

        <button type="button" class="btn btn-primary btn-block mb-4" style={{width: '100%'}} onClick={e => onSubmit(e)}>Sign in</button>
      </form>
    </div>
  )
}

export default Login