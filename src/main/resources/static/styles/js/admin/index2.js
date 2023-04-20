
document.getElementById('btn-update-product').addEventListener('click', async()=>{
  const productName = document.getElementById('productName').value;
  const productDescription = document.getElementById('productDescription').value;
  const priceNew = document.getElementById('priceNew').value;
  const priceOld = document.getElementById('priceOld').value;
  const categoryId = document.getElementById('categoryId').value;
  const id = document.getElementById('productId').value;
  const files = document.getElementById('file').files;
    const formData = new FormData();
    for (const file of files) {
      formData.append("file", file);
    }
    try {
      formData.append("upload_preset", "my_travel");
      const url = "https://api.cloudinary.com/v1_1/love3000/upload";
      const { data } = await axios.post(url, formData);
            const response = await axios.put(`http://localhost:8080/admin/product-update/${id}`,
              {
                productName,
                productDescription,
                priceNew,
                priceOld,
                categoryId,
                productImage: data.url,
              })

               window.location ="/admin/product-list"
   }catch(error){

   }
})