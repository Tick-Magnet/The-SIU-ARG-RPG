import React, {useState} from 'react';
import { Item } from './Item';
import { Combat } from './Combat';
import {Character } from './Character';
import Cookies from 'universal-cookie';

function App() {
  const [currentForm, setCurrentForm] = useState('Item');


  const toggleForm = (formName) => {
    setCurrentForm(formName);
  }

  return (
    <div className='App'>
      {
        currentForm === "Item" ? <Character onFormSwitch={toggleForm}/> : <Combat  onFormSwitch={toggleForm}/>
      }

    </div>
  )

}

export default App;
