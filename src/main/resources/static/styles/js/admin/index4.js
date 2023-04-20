
console.log('hello world');

const increment =  document.getElementsByClassName("increment")[0];
const quantity = document.getElementsByClassName("quantity-item")[0];
const decrement = document.getElementsByClassName("decrement")[0]

increment.addEventListener('click',()=>{
    let value = parseInt(quantity.innerHTML) + 1;
    quantity.innerHTML = value;
})

decrement.addEventListener('click',()=>{
    let value = parseInt(quantity.innerHTML);
    if(parseInt(value) <= 1){
        return;
    }
    value = parseInt(quantity.innerHTML) - 1;
    quantity.innerHTML = value;
})