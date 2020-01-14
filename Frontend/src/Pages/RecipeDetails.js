import React, { useState, useEffect } from 'react';
import { useRouteMatch, Link } from 'react-router-dom';
import { Col, Row, ListGroup } from 'react-bootstrap'
import facade from '../login/ApiFacade'

function RecipeDetails() {
    let match = useRouteMatch();
    const [recipe, setRecipe] = useState({ingredients: [], preparaion_steps: []});

    useEffect(() => {
        facade.fetchRecipeByName(match.params.name).then(res => setRecipe(res));
    }, [match.params.name])

    return (
        <div className="container">
            <Link to="/" className="btn btn-primary">Back</Link>
            <Row>
                <Col sm={12}>
                    <h1>{recipe.id} - {recipe.prep_time}</h1>
                </Col>
                <hr/>
                <Col sm={12}>
                    <p>{recipe.description}</p>
                </Col>
                <br/><br />
                <Row>
                    <Col sm={6}>
                        <h1>Ingredients</h1>
                        <ListGroup variant="flush">
                        {recipe.ingredients.map((ingredient, index) => (
                            <ListGroup.Item key={index}>{ingredient}</ListGroup.Item>
                        ))}
                        </ListGroup>
                    </Col>
                    <Col sm={6}>
                        <h1>Preperation</h1>
                        <ListGroup variant="flush">
                        {recipe.preparaion_steps.map((step, index) => (
                            <ListGroup.Item key={index}>{step}</ListGroup.Item>
                        ))}
                        </ListGroup>
                    </Col>
                </Row>
            </Row>
        </div>
    )
}

export default RecipeDetails;