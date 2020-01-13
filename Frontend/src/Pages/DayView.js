import React, { useState } from 'react';
import { Form, Table} from 'react-bootstrap'
import { Redirect } from 'react-router-dom';
import URLSettings from '../settings'

export default function DayView({ day, recipes, onChange, week }) {
    const [recipe, setRecipe] = useState({});
  
    const recipeClicked = (recipe) => {
      setRecipe(recipe);
    }
  
    const createTableData = () => {
      let data = [];
  
      recipes.forEach((recipe, index) => {
        data.push(
          <tr key={index} style={week[day] === recipe.id ? {"backgroundColor": "#007bff"} : {}}>
            <td>
              <Form.Check
                data-key={day}
                type="radio"
                name="recipe"
                id="Test"
                value={recipe.id}
                checked={week[day] === recipe.id}
                onChange={onChange}
              />
            </td>
            <td onClick={() => recipeClicked(recipe)}>{recipe.id}</td>
            <td onClick={() => recipeClicked(recipe)}>{recipe.prep_time}</td>
          </tr>
        )
      });
  
      return data;
    }
  
    return (
      <div>
        {recipe.id !== undefined ? <Redirect to={URLSettings.getURL("ProductDetails") + "/" + recipe.id} /> : ""}
        <Form>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Selected</th>
                <th>Name</th>
                <th>Preperation time</th>
              </tr>
            </thead>
            <tbody>
              {createTableData()}
            </tbody>
          </Table>
        </Form>
      </div>
    )
  }