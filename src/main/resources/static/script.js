const token = sessionStorage.getItem('token');
console.log('token' , token)
if (!token || token == "") {
    window.location.href = '/login.html';
}
const customerForm = document.getElementById('customerForm');
const customerTableBody = document.getElementById('customerTable').querySelector('tbody');
const customerIdInput = document.getElementById('customerId');
const editCustomerModal = document.getElementById('editCustomerModal');
const editCustomerIdInput = document.getElementById('editCustomerId');
const editCustomerForm = document.getElementById('editCustomerForm');

customerForm.addEventListener('submit', handleFormSubmit);
editCustomerForm.addEventListener('submit', handleEditFormSubmit);

async function handleFormSubmit(event) {
    event.preventDefault();
    event.stopPropagation();

    const customer = {
        first_name: document.getElementById('first_name').value,
        last_name: document.getElementById('last_name').value,
        street: document.getElementById('street').value,
        address: document.getElementById('address').value,
        city: document.getElementById('city').value,
        state: document.getElementById('state').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
    };
    const url =`http://localhost:8080/customers/create`;

    let res = await fetch(url,
        {
            method: 'POST',
            headers: {
                'Authorization' : `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        }
    );
    res = await res.json();

    if(res == 'user is added'){
        customerForm.reset();
        await fetchCustomers();
        customerIdInput.value = '';
    }
    console.log(res);
}
fetchCustomers();

async function fetchCustomers(page = 1, limit = 10) {
let response = await fetch(`http://localhost:8080/customers/find?limit=${limit}&page=${page}`, { headers: {'Authorization' : `Bearer ${token}`, 'Content-Type': 'application/json'} });
this.customers = await response.json();
customerTableBody.innerHTML = '';
            this.customers.forEach(customer => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${customer.first_name}</td>
                    <td>${customer.last_name}</td>
                    <td>${customer.street}</td>
                    <td>${customer.address}</td>
                    <td>${customer.city}</td>
                    <td>${customer.state}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editCustomer(${customer.id})">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteCustomer(${customer.id})">Delete</button>
                    </td>
                `;
                customerTableBody.appendChild(row);
            });
}


window.deleteCustomer = async function(id) {
    if (confirm('Are you sure you want to delete this customer?')) {
        await fetch(`http://localhost:8080/customers/remove?id=${id}`, {
            method: 'DELETE'
            headers: {
                'Authorization' : `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
        });
        await this.fetchCustomers();
    }
}

window.editCustomer = function(id){

//    let response = await fetch(`http://localhost:8080/customers/get/${id}` , {method: "GET", headers: {'Authorization' : `Bearer ${token}`, 'Content-Type': 'application/json'}});
    const customer = this.customers.find((x)=> x.id == id);
    if(customer){
        document.getElementById('editFirstName').value = customer.first_name;
        document.getElementById('editLastName').value = customer.last_name;
        document.getElementById('editStreet').value = customer.street;
        document.getElementById('editAddress').value = customer.address;
        document.getElementById('editCity').value = customer.city;
        document.getElementById('editState').value = customer.state;
        document.getElementById('editEmail').value = customer.email;
        document.getElementById('editPhone').value = customer.phone;
        editCustomerIdInput.value = customer.id;
    }
    editCustomerModal.classList.add('show');
    editCustomerModal.style.display = 'block';
}

async function handleEditFormSubmit(event) {

        event.preventDefault();
        event.stopPropagation();

        const customer = {
            first_name: document.getElementById('editFirstName').value,
            last_name: document.getElementById('editLastName').value,
            street: document.getElementById('editStreet').value,
            address: document.getElementById('editAddress').value,
            city: document.getElementById('editCity').value,
            state: document.getElementById('editState').value,
            email: document.getElementById('editEmail').value,
            phone: document.getElementById('editPhone').value,
        };

        console.log(customer);
        const url = `http://localhost:8080/customers/update/${editCustomerIdInput.value}`;

        let res = await fetch(url ,
        {
           method: 'PUT',
           headers: {
               'Authorization' : `Bearer ${token}`,
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(customer)
        });

       editCustomerModal.classList.remove('show');
       editCustomerModal.style.display = 'none';
       editCustomerForm.reset();
       editCustomerIdInput.value = '';
       await fetchCustomers();
}


function closeModel(){
    editCustomerModal.classList.remove('show');
    editCustomerModal.style.display = 'none';
}
