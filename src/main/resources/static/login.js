const loginForm = document.getElementById('loginForm');
loginForm.addEventListener('submit', login);
async function login(){

    event.preventDefault();
    event.stopPropagation();
    console.log('tesr');
    const user = {
        email : document.getElementById('username').value,
        password : document.getElementById('password').value
    }
    const url = 'http://localhost:8080/auth/login';
    let res = await fetch(url ,
    {
       method: 'POST',
       headers: {
           'Content-Type': 'application/json'
       },
       body: JSON.stringify(user)
    });

    res = await res.json();
    console.log(res);
    if(res && res.jwtToken){
        sessionStorage.setItem('token', res.jwtToken);
        window.location.href = '/index.html';
    }else{
        alert(res);
    }


}