import React, { useEffect, useState } from 'react'
import { todoData } from '../services/todoServices'

function ListTodos() {

    // const dummyData = [
    //     {"id" : 1, "title" :  "Title one" , "status" : false},
    //     {"id" : 2, "title" :  "Title two" , "status" : true},
    //     {"id" : 3, "title" :  "Title three" , "status" : false},
    //     {"id" : 4, "title" :  "Title four" , "status" : false}]


    // {
    //     "id": 52,
    //     "title": "\"Test4\"",
    //     "status": false
    //   },

    const [listOfTodos, setListOfTodos] = useState([])
    useEffect(() => {
      todoData().then((response) => {
        setListOfTodos(response.data);
      }).catch(error => console.log(error));
    }, [])

    console.log("result is from db " + listOfTodos);
        
    return (
    <div className='container mt-5' >
        <h2 className='text-center'>Todos</h2>
        <table className = 'table table-striped table-bordered mt-3'>
            <thead className='table-dark'>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                {
                    listOfTodos.map(data => 
                        <tr key={data.id}>
                            <td>{data.id}</td>
                            <td>{data.title}</td>
                            <td>{String(data.status)}</td>
                        </tr>)
                }
                
            </tbody>
        </table>
    </div>
  )
}

export default ListTodos