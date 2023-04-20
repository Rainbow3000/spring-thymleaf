const listPrices = document.getElementsByClassName('price-item');
for(let i = 0 ; i< listPrices.length ; i++){
  listPrices[i].innerHTML = parseFloat(listPrices[i].innerHTML).toLocaleString('it-IT', {style : 'currency', currency : 'VND'})
}