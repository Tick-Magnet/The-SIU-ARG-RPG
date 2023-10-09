import React, {useState} from 'react';
import { Item } from './Item';
import { Combat } from './Combat';

function App() {
  const [currentForm, setCurrentForm] = useState('Item');

  const toggleForm = (formName) => {
    setCurrentForm(formName);
  }

  return (
    <div className='App'>
      {
        currentForm === "Item" ? <Item onFormSwitch={toggleForm}/> : <Combat  onFormSwitch={toggleForm}/>
      }
    </div>
  )

}

export default App;