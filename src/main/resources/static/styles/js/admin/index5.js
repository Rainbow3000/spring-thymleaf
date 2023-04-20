


 const getChart = async()=>{
  const ctx = document.getElementById('myChart');
    try{
        const response = await axios.get('http://localhost:8080/charts');
        const dataResponse = response.data;
        const labels = dataResponse.map(item=>item.month);
        const data = dataResponse.map(item=>item.count);
        new Chart(ctx, {
            type: 'line',
            data: {
              labels: labels,
              datasets: [{
                label: '# of Votes',
                data: data,
                borderWidth: 1
              }]
            },
            options: {
              scales: {
                y: {
                  beginAtZero: true
                }
              }
            }
          });
    }catch(error){

    }
 }

 getChart();




