import React, { useState, useEffect } from 'react';
import { Form, Table, InputGroup, FormControl } from 'react-bootstrap'
import { Redirect } from 'react-router-dom';
import URLSettings from '../settings'

export default function DayView({ day, recipes, onChange, week }) {
    const [recipesData, setRecipesData] = useState([]);
    const [recipe, setRecipe] = useState({});

    useEffect(() => {
        setRecipesData(recipes);
    }, [recipes])
  
    const recipeClicked = (recipe) => {
      setRecipe(recipe);
    }
  
    const createTableData = () => {
      let data = [];
  
      recipesData.forEach((recipe, index) => {
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

    const onSearch = (evt) => {
        let newRecipeList = recipes.filter(r => r.id.toUpperCase().startsWith(evt.target.value.toUpperCase()));
        setRecipesData(newRecipeList);
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
                <InputGroup size="sm" className="mb-3">
                    <InputGroup.Prepend>
                    <InputGroup.Text>Name Search</InputGroup.Text>
                    </InputGroup.Prepend>
                    <FormControl type="text" data-key="week" placeholder="Search...." onChange={onSearch} />
                </InputGroup>
            </Form>
        </div>
    )
  }